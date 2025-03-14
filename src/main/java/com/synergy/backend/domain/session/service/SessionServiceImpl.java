package com.synergy.backend.domain.session.service;

import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.conference.exception.NotFoundConference;
import com.synergy.backend.domain.conference.repository.ConferenceRepository;
import com.synergy.backend.domain.member.entity.Admin;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.User;
import com.synergy.backend.domain.session.dto.SessionDetailResDto;
import com.synergy.backend.domain.session.dto.SessionReqDto;
import com.synergy.backend.domain.session.dto.SessionResDto;
import com.synergy.backend.domain.session.dto.sessionparticipate.SessionParticipateRateResDto;
import com.synergy.backend.domain.session.dto.question.QuestionReqDto;
import com.synergy.backend.domain.session.dto.question.QuestionResDto;
import com.synergy.backend.domain.session.entity.AttendeeSession;
import com.synergy.backend.domain.session.entity.Session;
import com.synergy.backend.domain.session.entity.SessionQuestion;
import com.synergy.backend.domain.session.exception.NotAttendedSession;
import com.synergy.backend.domain.session.exception.NotFoundSession;
import com.synergy.backend.domain.session.repository.AttendeeSessionRepository;
import com.synergy.backend.domain.session.repository.SessionQuestionRepository;
import com.synergy.backend.domain.session.repository.SessionRepository;
import com.synergy.backend.domain.session.service.validate.DateTimeValidator;
import com.synergy.backend.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final ConferenceRepository conferenceRepository;
    private final SessionRepository sessionRepository;
    private final AttendeeSessionRepository attendeeSessionRepository;
    private final SessionQuestionRepository sessionQuestionRepository;

    public User getCurrentMember() {
        return SecurityUtil.getCurrentMember();
    }

    @Override
    public void createSession(Long conferenceId, SessionReqDto reqDto) {
        Admin member = (Admin) getCurrentMember();
        Conference conference = ifConferenceExists(conferenceId);

        LocalDate progressDate = LocalDate.parse(reqDto.progressDate());
        LocalDateTime startTime = DateTimeValidator.isValidLocalDateTime(reqDto.startTime());
        LocalDateTime endTime = DateTimeValidator.isValidLocalDateTime(reqDto.endTime());

        Session session = Session.of(reqDto, progressDate, startTime, endTime, conference);
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
        Attendee attendee = (Attendee) getCurrentMember();
        ifConferenceExists(conferenceId);
        Session session = ifSessionExists(sessionId);

        //redis에 저장된 secretCode와 일치할 경우에 attendeeSession에 추가
        try {
            ifAttendeeSessionExists(sessionId, attendee.getId());
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
        Session session = ifSessionExists(sessionId);
        // session에 대한 본인 소지 여부 확인
        sessionRepository.delete(session);
    }

    // -------------------------------------- Q&A ------------------------------------------------

    @Override
    public void verifyQRCode(Long sessionId, String secretCode) {

    }

    @Transactional
    @Override
    public void createQuestion(Long conferenceId, Long sessionId, QuestionReqDto reqDto) {
        Attendee attendee = (Attendee) getCurrentMember();
        ifConferenceExists(conferenceId);
        AttendeeSession attendeeSession = ifAttendeeSessionExists(sessionId, attendee.getId());

        SessionQuestion question = SessionQuestion.of(reqDto.content());
        sessionQuestionRepository.save(question);

        attendeeSession.addSessionQuestion(question);
    }

    @Override
    public SessionParticipateRateResDto getSessionParticipateRate(Long conferenceId) {
        // 이걸 구현하려면 일자별 세션들의 참여율을 조회해야함.
        Admin admin =  (Admin) getCurrentMember();
        ifConferenceExists(conferenceId);


        return null;
    }

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
