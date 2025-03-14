package com.synergy.backend.domain.session.service;

import com.synergy.backend.domain.session.dto.SessionDetailResDto;
import com.synergy.backend.domain.session.dto.SessionReqDto;
import com.synergy.backend.domain.session.dto.SessionResDto;
import com.synergy.backend.domain.session.dto.sessionparticipate.SessionParticipateRateResDto;
import com.synergy.backend.domain.session.dto.question.QuestionReqDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SessionService {

    void createSession(Long conferenceId, SessionReqDto reqDto);

    List<SessionResDto> getSessions(Long conferenceId);

    SessionDetailResDto getSessionInfo(Long conferenceId, Long sessionId);

    void updateSession(Long sessionId, SessionReqDto reqDto);

    void deleteSession(Long sessionId);

    void createQuestion(Long conferenceId, Long sessionId, QuestionReqDto reqDto);

    void verifyQRCode(Long sessionId, String secretCode);

    SessionParticipateRateResDto getSessionParticipateRate(Long conferenceId);
}
