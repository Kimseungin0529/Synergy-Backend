package com.synergy.backend.domain.session.service;

import com.google.zxing.WriterException;
import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.conference.exception.NotFoundConference;
import com.synergy.backend.domain.conference.repository.ConferenceRepository;
import com.synergy.backend.domain.member.entity.Admin;
import com.synergy.backend.domain.member.entity.User;
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
import com.synergy.backend.global.util.SecurityUtil;
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

    private final ConferenceRepository conferenceRepository;
    private final SessionRepository sessionRepository;
    private final AttendeeSessionRepository attendeeSessionRepository;
    private final SessionQuestionRepository sessionQuestionRepository;
    private final FileS3Util fileS3Util;

    private final QrService qrService;

    private User getCurrentMember() {
        return SecurityUtil.getCurrentMember();
    }

    @Override
    public void createSession(Long conferenceId, SessionReqDto reqDto) throws WriterException {
        Admin admin = (Admin) getCurrentMember();
        Conference conference = ifConferenceExists(conferenceId);

        LocalDate progressDate = LocalDate.parse(reqDto.progressDate());
        LocalDateTime startTime = DateTimeValidator.isValidLocalDateTime(reqDto.startTime());
        LocalDateTime endTime = DateTimeValidator.isValidLocalDateTime(reqDto.endTime());
        String secretCode = UUID.randomUUID().toString();

        Session session = Session.of(reqDto, progressDate, startTime, endTime, conference);
        byte[] bytes = qrService.generateQRCode(reqDto.domainAddress(), session.getId(), secretCode);
        admin.addSession(session);
        session.addQRCode(bytes);
        sessionRepository.save(session);
    }

    @Transactional(readOnly = true)
    @Override
    public List<SessionResDto> getSessions(Long conferenceId) {
        Conference conference = ifConferenceExists(conferenceId);
        List<Session> sessions = sessionRepository.findAllByConferenceOrderByStartTime(conference);

        return sessions.stream().map(SessionResDto::from).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public SessionDetailResDto getSessionInfo(Long conferenceId, Long sessionId) {
        User user = getCurrentMember();
        ifConferenceExists(conferenceId);
        Session session = ifSessionExists(sessionId);

        try {
            ifAttendeeSessionExists(sessionId, user.getId());
            List<QuestionResDto> questions = getQuestions(conferenceId, sessionId);
            return SessionDetailResDto.from(session, questions);
        } catch (Exception e) {
            return SessionDetailResDto.from(session, null);
        }
    }

    @Override
    public void updateSession(Long sessionId, SessionReqDto reqDto) {
        Session session = ifSessionExists(sessionId);
        // session에 대한 본인 소지 여부 확인
        LocalDate progressDate = LocalDate.parse(reqDto.progressDate());
        LocalDateTime startTime = DateTimeValidator.isValidLocalDateTime(reqDto.startTime());
        LocalDateTime endTime = DateTimeValidator.isValidLocalDateTime(reqDto.endTime());

        session.updateSession(reqDto, progressDate, startTime, endTime);
    }

    @Override
    public void deleteSession(Long sessionId) {
        User user = getCurrentMember();
        // session에 대한 권한 확인
        Session session = ifSessionExists(sessionId);
        sessionRepository.delete(session);
    }

    // --------------------------------- private method ----------------------------------------

    private List<QuestionResDto> getQuestions(Long conferenceId, Long sessionId) {
        getCurrentMember();
        ifConferenceExists(conferenceId);
        ifSessionExists(sessionId);

        return sessionQuestionRepository.findBySessionIdJoinAttendeeSession(sessionId);
    }

    private AttendeeSession ifAttendeeSessionExists(Long sessionId, Long attendeeId) {
        return attendeeSessionRepository.findBySessionAndAttendeeId(sessionId, attendeeId)
                .orElseThrow(NotAttendedSession::new);
    }

    private Conference ifConferenceExists(Long conferenceId) {
        return conferenceRepository.findById(conferenceId).orElseThrow(NotFoundConference::new);
    }

    private Session ifSessionExists(Long sessionId) {
        return sessionRepository.findById(sessionId).orElseThrow(NotFoundSession::new);
    }

}
