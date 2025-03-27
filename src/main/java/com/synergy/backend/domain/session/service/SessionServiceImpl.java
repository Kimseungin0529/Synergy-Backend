package com.synergy.backend.domain.session.service;

import com.google.zxing.WriterException;
import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.conference.exception.NotFoundConference;
import com.synergy.backend.domain.conference.repository.ConferenceRepository;
import com.synergy.backend.domain.member.entity.Admin;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.RoleType;
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
import com.synergy.backend.global.exception.AuthorizedException;
import com.synergy.backend.global.util.file.dto.FileInformationDto;
import com.synergy.backend.global.util.file.util.FileS3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
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

    @Transactional
    @Override
    public void createSession(String identifier, String router, Long conferenceId, SessionReqDto reqDto, MultipartFile multipartFile) throws WriterException {
        Admin admin = findIfAdminExists(identifier);
        Conference conference = ifConferenceExists(conferenceId);

        String secretCode = UUID.randomUUID().toString();

        Session session = Session.of(reqDto, secretCode, conference);
        admin.addSession(session);

        sessionRepository.save(session);

        String url = router + "session/" + session.getId();
        byte[] qrCode = qrService.generateQRCode(url, secretCode);
        log.info("sessionId: {}", session.getId());
        log.info("qrCode: {}", qrCode);
        session.addQRCode(fileS3Util.uploadQRCode(qrCode, session.getTitle()));
        if(multipartFile != null) {
            session.addImage(fileS3Util.uploadFile(multipartFile));
        }
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
        Conference conference = ifConferenceExists(conferenceId);
        Session session = findByConferenceId(sessionId, conference);

        try {
            if(role.equals(RoleType.ATTENDEE)) {
                Attendee attendee = findIfAttendeeExists(identifier);
                ifAttendeeSessionExists(sessionId, attendee.getId());
            }
            List<QuestionResDto> questions = getQuestions(conference, sessionId);
            return SessionDetailResDto.from(session, questions);
        } catch (Exception e) {
            return SessionDetailResDto.from(session, List.of());
        }
    }

    @Transactional
    @Override
    public void updateSession(String identifier, Long sessionId, SessionReqDto reqDto, MultipartFile multipartFile) {
        Session session = ifSessionExists(sessionId);
        Admin admin = findIfAdminExists(identifier);
        verifyAuthenticationRole(session, admin);

        FileInformationDto fileInfo = new FileInformationDto(null, null);
        if(multipartFile != null) {
            fileInfo = fileS3Util.uploadFile(multipartFile);
        }
        session.updateSession(reqDto, fileInfo);
    }

    @Override
    public void deleteSession(String identifier, Long sessionId) {
        Session session = ifSessionExists(sessionId);
        Admin admin = findIfAdminExists(identifier);
        verifyAuthenticationRole(session, admin);

        session.removeAllAdmins();

        sessionRepository.delete(session);
    }

    // --------------------------------- private method ----------------------------------------

    private Session findByConferenceId(Long sessionId, Conference conference) {
        return sessionRepository.findByIdAndConference(sessionId, conference).orElseThrow(NotFoundSession::new);
    }

    private Admin findIfAdminExists(String identifier) {
        return adminRepository.findByAdminAuthCode(identifier).orElseThrow(NotFoundUserException::new);
    }

    private Attendee findIfAttendeeExists(String identifier) {
        return attendeeRepository.findByEmail(identifier).orElseThrow(NotFoundUserException::new);
    }

    private List<QuestionResDto> getQuestions(Conference conference, Long sessionId) {
        findByConferenceId(sessionId, conference);

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

    private void verifyAuthenticationRole(Session session, Admin admin) {
        if(!sessionRepository.existsByIdAndAdmins_Id(session.getId(), admin.getId())){
            throw new AuthorizedException();
        }
    }

}
