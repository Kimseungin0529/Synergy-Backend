package com.synergy.backend.domain.session.service;

import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.conference.exception.NotFoundConference;
import com.synergy.backend.domain.conference.repository.ConferenceRepository;
import com.synergy.backend.domain.member.entity.Admin;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.User;
import com.synergy.backend.domain.member.repository.AdminRepository;
import com.synergy.backend.domain.session.dto.sessionDto.SessionResDto;
import com.synergy.backend.domain.session.dto.questionDto.QuestionReqDto;
import com.synergy.backend.domain.session.dto.sessionparticipateDto.SessionParticipateRateDetailResDto;
import com.synergy.backend.domain.session.dto.sessionparticipateDto.SessionParticipateRateResDto;
import com.synergy.backend.domain.session.entity.AttendeeSession;
import com.synergy.backend.domain.session.entity.Session;
import com.synergy.backend.domain.session.entity.SessionQuestion;
import com.synergy.backend.domain.session.exception.InvalidTimeException;
import com.synergy.backend.domain.session.exception.NotAttendedSession;
import com.synergy.backend.domain.session.exception.NotFoundSession;
import com.synergy.backend.domain.session.repository.AttendeeSessionRepository;
import com.synergy.backend.domain.session.repository.sessionQuestionRepository.SessionQuestionRepository;
import com.synergy.backend.domain.session.repository.sessionRepository.SessionRepository;
import com.synergy.backend.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionParticipateServiceImpl implements SessionParticipateService {

    private final SessionRepository sessionRepository;
    private final SessionQuestionRepository sessionQuestionRepository;
    private final ConferenceRepository conferenceRepository;
    private final AttendeeSessionRepository attendeeSessionRepository;

    private User getCurrentMember(){
        return SecurityUtil.getCurrentMember();
    }

    @Override
    public SessionResDto verifyQRCode(String secretCode) {
        Attendee currentMember = (Attendee) getCurrentMember();
        Session session = findBySecretCode(secretCode);

        AttendeeSession attendeeSession = AttendeeSession.of(currentMember, session);
        attendeeSessionRepository.save(attendeeSession);
        return SessionResDto.from(session);
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
    public List<SessionParticipateRateResDto> getSessionParticipateRate(Long conferenceId) {
        Admin currentMember = (Admin) getCurrentMember();
        verifyUserAuthentication(currentMember); // 해당 컨퍼런스의 소유자인지 확인해야돰.
        ifConferenceExists(conferenceId);

        List<SessionParticipateRateResDto> sessionParticipate =
                sessionRepository.getSessionParticipateByConferenceId(conferenceId);
        if(sessionParticipate.isEmpty()) {
            throw new InvalidTimeException();
        }

        return sessionParticipate;
    }

    @Override
    public SessionParticipateRateDetailResDto getSessionParticipateRateDetail(Long conferenceId) {
        //
        Admin currentMember = (Admin) getCurrentMember();
        verifyUserAuthentication(currentMember); // 해당 컨퍼런스의 소유자인지 확인해야돰.


        return null;
    }

    private void verifyUserAuthentication(Admin admin){
    }

    private Session findBySecretCode(String secretCode) {
        return sessionRepository.findBySecretCode(secretCode).orElseThrow(NotFoundSession::new);
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
