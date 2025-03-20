package com.synergy.backend.domain.session.service;

import com.google.zxing.WriterException;
import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.conference.exception.NotFoundConference;
import com.synergy.backend.domain.conference.repository.ConferenceRepository;
import com.synergy.backend.domain.member.entity.Admin;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.domain.member.entity.User;
import com.synergy.backend.domain.member.exception.NotFoundUserException;
import com.synergy.backend.domain.member.repository.AdminRepository;
import com.synergy.backend.domain.member.repository.AttendeeRepository;
import com.synergy.backend.domain.qrCode.service.QrService;
import com.synergy.backend.domain.session.dto.sessionDto.SessionDetailResDto;
import com.synergy.backend.domain.session.dto.sessionDto.SessionReqDto;
import com.synergy.backend.domain.session.dto.sessionDto.SessionResDto;
import com.synergy.backend.domain.session.dto.questionDto.QuestionResDto;
import com.synergy.backend.domain.session.entity.AttendeeSession;
import com.synergy.backend.domain.session.entity.Session;
import com.synergy.backend.domain.session.exception.NotAttendedSession;
import com.synergy.backend.domain.session.exception.NotFoundSession;
import com.synergy.backend.domain.session.repository.AttendeeSessionRepository;
import com.synergy.backend.domain.session.repository.sessionQuestionRepository.SessionQuestionRepository;
import com.synergy.backend.domain.session.repository.sessionRepository.SessionRepository;
import com.synergy.backend.domain.session.service.validate.DateTimeValidator;
import com.synergy.backend.global.util.SecurityUtils;
import com.synergy.backend.global.util.file.util.FileS3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final AdminRepository adminRepository;
    private final ConferenceRepository conferenceRepository;
    private final SessionRepository sessionRepository;
    private final AttendeeSessionRepository attendeeSessionRepository;
    private final SessionQuestionRepository sessionQuestionRepository;
    private final AttendeeRepository attendeeRepository;
    private final FileS3Util fileS3Util;

    private final QrService qrService;

    @Override
    public void createSession(String idenfier, Long conferenceId, SessionReqDto reqDto) throws WriterException {
        Admin admin = findIfAdminExists(idenfier);
        Conference conference = ifConferenceExists(conferenceId);

        LocalDate progressDate = LocalDate.parse(reqDto.progressDate());
        LocalDateTime startTime = DateTimeValidator.isValidLocalDateTime(reqDto.startTime());
        LocalDateTime endTime = DateTimeValidator.isValidLocalDateTime(reqDto.endTime());
        String secretCode = UUID.randomUUID().toString();

        Session session = Session.of(reqDto, progressDate, startTime, endTime, secretCode, conference);
        admin.addSession(session);
        byte[] qrCode = qrService.generateQRCode(reqDto.domainAddress(), session.getId(), secretCode);
        session.addQRCode(fileS3Util.uploadQRCode(qrCode, session.getTitle()));

        sessionRepository.save(session);
    }

    @Transactional(readOnly = true)
    @Override
    public List<SessionResDto> getSessions(String identifier, Long conferenceId) {
        Conference conference = ifConferenceExists(conferenceId);
        List<Session> sessions = sessionRepository.findAllByConferenceOrderByStartTime(conference);

        return sessions.stream().map(SessionResDto::from).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public SessionDetailResDto getSessionInfo(String identifier, RoleType role, Long conferenceId, Long sessionId) {
        ifConferenceExists(conferenceId);
        Session session = ifSessionExists(sessionId);

        try {
            if(role.equals(RoleType.ATTENDEE)) {
                Attendee attendee = findIfAttendeeExists(identifier);
                ifAttendeeSessionExists(sessionId, attendee.getId());
            }
            List<QuestionResDto> questions = getQuestions(conferenceId, sessionId);
            return SessionDetailResDto.from(session, questions);
        } catch (Exception e) {
            return SessionDetailResDto.from(session, null);
        }
    }

    @Override
    public void updateSession(String idenfier, Long sessionId, SessionReqDto reqDto) {
        Session session = ifSessionExists(sessionId);
        // session에 대한 본인 소지 여부 확인
        LocalDate progressDate = LocalDate.parse(reqDto.progressDate());
        LocalDateTime startTime = DateTimeValidator.isValidLocalDateTime(reqDto.startTime());
        LocalDateTime endTime = DateTimeValidator.isValidLocalDateTime(reqDto.endTime());

        session.updateSession(reqDto, progressDate, startTime, endTime);
    }

    @Override
    public void deleteSession(String idenfier, Long sessionId) {
        Admin admin = findIfAdminExists(idenfier);
        // session에 대한 권한 확인
        Session session = ifSessionExists(sessionId);
        sessionRepository.delete(session);
    }

    // --------------------------------- private method ----------------------------------------

    private Admin findIfAdminExists(String identifier) {
        return adminRepository.findByAdminAuthCode(identifier).orElseThrow(NotFoundUserException::new);
    }

    private Attendee findIfAttendeeExists(String identifier) {
        return attendeeRepository.findByEmail(identifier).orElseThrow(NotFoundUserException::new);
    }

    private List<QuestionResDto> getQuestions(Long conferenceId, Long sessionId) {
        ifConferenceExists(conferenceId);
        ifSessionExists(sessionId);

        return sessionQuestionRepository.findBySessionIdJoinAttendeeSession(sessionId);
    }

    private AttendeeSession ifAttendeeSessionExists(Long sessionId, Long attendeeId) {
        return attendeeSessionRepository.findBySessionIdAndAttendeeId(sessionId, attendeeId)
                .orElseThrow(NotAttendedSession::new);
    }

    private Conference ifConferenceExists(Long conferenceId) {
        return conferenceRepository.findById(conferenceId).orElseThrow(NotFoundConference::new);
    }

    private Session ifSessionExists(Long sessionId) {
        return sessionRepository.findById(sessionId).orElseThrow(NotFoundSession::new);
    }

}
