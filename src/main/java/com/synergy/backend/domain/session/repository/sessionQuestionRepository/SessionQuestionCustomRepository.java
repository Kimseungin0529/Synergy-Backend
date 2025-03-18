package com.synergy.backend.domain.session.repository.sessionQuestionRepository;

import com.synergy.backend.domain.session.dto.questionDto.QuestionResDto;

import java.util.List;

public interface SessionQuestionCustomRepository {

    List<QuestionResDto> findBySessionIdJoinAttendeeSession(Long sessionId);


}
