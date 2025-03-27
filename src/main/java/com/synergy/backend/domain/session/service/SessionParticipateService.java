package com.synergy.backend.domain.session.service;

import com.synergy.backend.domain.session.dto.sessionDto.SessionResDto;
import com.synergy.backend.domain.session.dto.questionDto.QuestionReqDto;
import com.synergy.backend.domain.session.dto.sessionparticipateDto.SessionParticipateRateDetailResDto;
import com.synergy.backend.domain.session.dto.sessionparticipateDto.SessionParticipateRateResDto;

import java.util.List;

public interface SessionParticipateService {

    void createQuestion(String identifier, Long sessionId, QuestionReqDto reqDto);

    SessionResDto verifyQRCode(String identifier, Long sessionId, String secretCode);

    List<SessionParticipateRateResDto> getSessionParticipateRate(String identifier, Long conferenceId);

    List<SessionParticipateRateDetailResDto> getSessionParticipateRateDetail(String identifier, Long conferenceId);
}
