package com.synergy.backend.domain.session.repository.sessionRepository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.synergy.backend.domain.session.dto.sessionparticipateDto.SessionParticipateRateDetailResDto;
import com.synergy.backend.domain.session.dto.sessionparticipateDto.SessionParticipateRateResDto;
import com.synergy.backend.domain.session.dto.sessionparticipateDto.SessionParticipateTechResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.synergy.backend.domain.interest.entity.QAttendeeInterest.attendeeInterest;
import static com.synergy.backend.domain.interest.entity.QInterest.interest;
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
                .from(attendeeSession)
                .join(attendeeSession.session, session)
                .where(session.conference.id.eq(conferenceId))
                .where(session.progressDate.eq(now))
                .groupBy(session.id)
                .orderBy(session.startTime.asc())
                .fetch();
    }

    // 컨퍼런스에 대한 각 세션들의 정보를 담음
    @Override
    public List<SessionParticipateRateDetailResDto> getSessionParticipateDetailByConferenceId(Long conferenceId) {
        List<Tuple> tuples = queryFactory
                .select(session.id, session.title, session.progressDate, session.startTime, session.endTime, session.qrUrl)
                .from(session)
                .where(session.conference.id.eq(conferenceId))
                .groupBy(session.id)
                .fetch();


        return tuples.stream()
                .map(tuple -> {
                    Long sessionId = tuple.get(session.id);
                    String title = tuple.get(session.title);
                    LocalDate progressDate = tuple.get(session.progressDate);
                    LocalDateTime startTime = tuple.get(session.startTime);
                    LocalDateTime endTime = tuple.get(session.endTime);
                    String fileUrl = tuple.get(session.qrUrl);

                    // 해당 세션의 기술 참여 인원 정보를 조회
                    List<SessionParticipateTechResDto> techDetails =
                            getSessionParticipateTechByConferenceIdAndSessionId(conferenceId, sessionId);

                    // record는 immutable하므로 모든 정보를 생성자에 넣어서 새 인스턴스 생성
                    return new SessionParticipateRateDetailResDto(
                            sessionId, title, progressDate, startTime, endTime, fileUrl, techDetails);
                })
                .toList();
    }

    // 특정 세션에 대한 분야별 참여 인원들을 담음.
    private List<SessionParticipateTechResDto> getSessionParticipateTechByConferenceIdAndSessionId(Long conferenceId, Long sessionId) {
        return queryFactory
                .select(Projections.constructor(
                        SessionParticipateTechResDto.class,
                        interest.name,
                        interest.count()
                ))
                .from(attendeeSession)
                .join(attendeeSession.session, session)
                .leftJoin(attendeeInterest).on(attendeeInterest.attendee.eq(attendeeSession.attendee))
                .join(attendeeInterest.interest, interest)
                .where(session.conference.id.eq(conferenceId))
                .where(session.id.eq(sessionId))
                .groupBy(attendeeInterest.interest)
                .fetch();
    }

}
