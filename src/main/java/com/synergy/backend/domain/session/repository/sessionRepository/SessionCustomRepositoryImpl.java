package com.synergy.backend.domain.session.repository.sessionRepository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.synergy.backend.domain.session.dto.sessionparticipateDto.SessionParticipateRateResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.synergy.backend.domain.conference.entity.QConference.conference;
import static com.synergy.backend.domain.session.entity.QAttendeeSession.attendeeSession;
import static com.synergy.backend.domain.session.entity.QSession.session;

@Repository
@RequiredArgsConstructor
public class SessionCustomRepositoryImpl implements SessionCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<SessionParticipateRateResDto> getSessionParticipateByConferenceId(Long conferenceId) {
        // 해당 컨퍼런스에 해당하는 모든 세션들에 대한 최대 인원 수용과 현재 attendeeSession있는 sessionId의 인원수를 담아 넣어놓는다.
        LocalDate now = LocalDate.now();

        return queryFactory
                .select(Projections.constructor(
                        SessionParticipateRateResDto.class,
                        session.title,
                        attendeeSession.count().as("attendeeCount"),
                        session.maximum
                ))
                .from(session)
                .leftJoin(session.attendeeSessions, attendeeSession).on(attendeeSession.session.id.eq(session.id))
                .where(session.conference.id.eq(conferenceId))
                .where(session.progressDate.eq(now))
                .fetch();
    }
}
