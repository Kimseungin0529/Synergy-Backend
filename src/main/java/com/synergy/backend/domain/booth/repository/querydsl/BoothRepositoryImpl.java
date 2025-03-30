package com.synergy.backend.domain.booth.repository.querydsl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.synergy.backend.domain.booth.dto.boothParticipateDto.*;
import com.synergy.backend.domain.member.repository.AttendeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.synergy.backend.domain.booth.entity.QBooth.booth;
import static com.synergy.backend.domain.booth.entity.QBoothParticipation.boothParticipation;
import static com.synergy.backend.domain.conference.entity.QConference.conference;
import static com.synergy.backend.domain.interest.entity.QAttendeeInterest.attendeeInterest;
import static com.synergy.backend.domain.interest.entity.QInterest.interest;
import static com.synergy.backend.domain.member.entity.QAttendee.attendee;

@Slf4j
@RequiredArgsConstructor
public class BoothRepositoryImpl implements BoothRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final AttendeeRepository attendeeRepository;
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");

    @Override
    public BoothParticipateRateResDto searchBoothRank(Long conferenceId, LocalDate currentDate) {
        Long totalCount = attendeeRepository.count();

        List<Tuple> tuples = queryFactory
                .select(
                        booth.id,
                        boothParticipation.count(),
                        booth.companyName
                )
                .from(booth)
                .leftJoin(boothParticipation).on(booth.id.eq(boothParticipation.booth.id))
                .where(
                        booth.conference.id.eq(conferenceId),
                        booth.progressDate.eq(currentDate)
                )
                .groupBy(booth.id)
                .having(boothParticipation.count().ne(0L))
                .orderBy(boothParticipation.count().desc())
                .limit(3)
                .fetch();

        List<BoothParticipateDetailDto> list = tuples.stream()
                .map(tuple -> {
                    Long boothId = tuple.get(booth.id);
                    Long attendeeCount = tuple.get(boothParticipation.count());
                    String companyName = tuple.get(booth.companyName);

                    return new BoothParticipateDetailDto(boothId, totalCount, attendeeCount, companyName);
                }).toList();

        return new BoothParticipateRateResDto(currentDate, list);
    }

    @Override
    public List<BoothParticipationInterestedResponseDto> searchBoothParticipation(Long conferenceId) {
        List<BoothParticipationInterestedResponseDto> booths = queryFactory
                .select(new QBoothParticipationInterestedResponseDto(
                        booth.id,
                        booth.companyName,
                        booth.companyType,
                        booth.boothLocation,
                        booth.boothNumber,
                        booth.progressDate,
                        booth.qrUrl
                ))
                .from(booth)
                .join(booth.conference, conference)
                .where(eqConference(conferenceId))
                .fetch();

        // 2. 부스별 관심 기술 카운트 조회
        List<BoothParticipateInterestedTechDto> techCounts = queryFactory
                .select(new QBoothParticipateInterestedTechDto(
                        booth.id,
                        interest.name.count(),
                        interest.name
                ))
                .from(boothParticipation)
                .join(boothParticipation.booth, booth)
                .join(boothParticipation.attendee, attendee)
                .join(attendee.attendeeInterests, attendeeInterest)
                .join(attendeeInterest.interest, interest)
                .where(booth.conference.id.eq(conferenceId))
                .groupBy(booth.id, interest.name)
                .fetch();


        // 3. boothId 기준으로 tech들을 그룹핑
        Map<Long, List<BoothParticipateInterestedTechDto>> techsByBooth = techCounts.stream()
                .collect(Collectors.groupingBy(BoothParticipateInterestedTechDto::getBoothId));

        // 4. 각 부스 DTO에 해당 tech 리스트 주입
        for (BoothParticipationInterestedResponseDto boothDto : booths) {
            boothDto.addTechs(techsByBooth.getOrDefault(boothDto.getBoothId(), List.of()));
        }

        return booths;
    }

    private BooleanExpression eqConference(Long conferenceId) {
        return booth.conference.id.eq(conferenceId);

    }



}
