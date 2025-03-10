package com.synergy.backend.domain.session.service;

import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.conference.exception.NotFoundConference;
import com.synergy.backend.domain.conference.repository.ConferenceRepository;
import com.synergy.backend.domain.session.dto.SessionDetailResDto;
import com.synergy.backend.domain.session.dto.SessionReqDto;
import com.synergy.backend.domain.session.dto.SessionResDto;
import com.synergy.backend.domain.session.entity.Session;
import com.synergy.backend.domain.session.exception.NotFoundSession;
import com.synergy.backend.domain.session.repository.SessionRepository;
import com.synergy.backend.domain.session.service.validate.DateTimeValidator;
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

    @Override
    public void createSession(Long conferenceId, SessionReqDto reqDto) {
        Conference conference = ifConferenceExists(conferenceId);

        LocalDateTime startTime = DateTimeValidator.isValidLocalDateTime(reqDto.startTime());
        LocalDateTime endTime = DateTimeValidator.isValidLocalDateTime(reqDto.endTime());
        LocalDate progressDate = LocalDate.parse(reqDto.progressDate());

        Session session = Session.builder()
                        .reqDto(reqDto)
                        .progressDate(progressDate)
                        .startTime(startTime)
                        .endTime(endTime)
                        .conference(conference)
                    .build();
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
        ifConferenceExists(conferenceId);
        Session session = ifSessionExists(sessionId);
        return SessionDetailResDto.from(session);
    }

    @Transactional
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

    private Conference ifConferenceExists(Long conferenceId) {
        return conferenceRepository.findById(conferenceId).orElseThrow(NotFoundConference::new);
    }

    private Session ifSessionExists(Long sessionId) {
        return sessionRepository.findById(sessionId).orElseThrow(NotFoundSession::new);
    }

}
