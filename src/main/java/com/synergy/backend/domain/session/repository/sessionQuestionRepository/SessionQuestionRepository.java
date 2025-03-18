package com.synergy.backend.domain.session.repository.sessionQuestionRepository;

import com.synergy.backend.domain.session.entity.SessionQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionQuestionRepository extends JpaRepository<SessionQuestion, Long>, SessionQuestionCustomRepository{
}
