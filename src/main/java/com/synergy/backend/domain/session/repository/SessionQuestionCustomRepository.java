package com.synergy.backend.domain.session.repository;

import com.synergy.backend.domain.session.dto.question.QuestionResDto;
import com.synergy.backend.domain.session.entity.SessionQuestion;

import java.util.List;

public interface SessionQuestionCustomRepository {

    List<QuestionResDto> findBySessionIdJoinAttendeeSession(Long sessionId);
}
