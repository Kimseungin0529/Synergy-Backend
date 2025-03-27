package com.synergy.backend.domain.booth.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.synergy.backend.domain.booth.dto.BoothParticipationResponseDto;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BoothRepositoryImpl implements BoothRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BoothParticipationResponseDto> searchBoothParticipation(Long conferenceId) {

        return null;
    }


    /*@Override
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
    }*/
}
