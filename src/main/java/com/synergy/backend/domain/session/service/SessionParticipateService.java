package com.synergy.backend.domain.session.service;

import com.synergy.backend.domain.session.dto.sessionDto.SessionResDto;
import com.synergy.backend.domain.session.dto.questionDto.QuestionReqDto;
import com.synergy.backend.domain.session.dto.sessionparticipateDto.SessionParticipateRateDetailResDto;
import com.synergy.backend.domain.session.dto.sessionparticipateDto.SessionParticipateRateResDto;

import java.util.List;

public interface SessionParticipateService {

    void createQuestion(Long conferenceId, Long sessionId, QuestionReqDto reqDto);

    SessionResDto verifyQRCode(String secretCode);

    List<SessionParticipateRateResDto> getSessionParticipateRate(Long conferenceId);

    List<SessionParticipateRateDetailResDto> getSessionParticipateRateDetail(Long conferenceId);
}
