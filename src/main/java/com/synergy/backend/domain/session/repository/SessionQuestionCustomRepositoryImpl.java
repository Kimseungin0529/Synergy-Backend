package com.synergy.backend.domain.session.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.synergy.backend.domain.session.dto.question.QuestionResDto;
import com.synergy.backend.domain.session.entity.SessionQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.synergy.backend.domain.member.entity.QAttendee.attendee;
import static com.synergy.backend.domain.session.entity.QAttendeeSession.attendeeSession;
import static com.synergy.backend.domain.session.entity.QSessionQuestion.sessionQuestion;

@RequiredArgsConstructor
@Repository
public class SessionQuestionCustomRepositoryImpl implements SessionQuestionCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<QuestionResDto> findBySessionIdJoinAttendeeSession(Long sessionId) {
        return queryFactory
                .select(Projections.constructor(
                        QuestionResDto.class,
                        sessionQuestion.id,
                        attendee.name,
                        sessionQuestion.question
                ))
                .from(sessionQuestion)
                .join(sessionQuestion.attendeeSession, attendeeSession)
                .join(attendeeSession.attendee, attendee)
                .where(attendeeSession.session.id.eq(sessionId))
                .fetch();

    }
}
