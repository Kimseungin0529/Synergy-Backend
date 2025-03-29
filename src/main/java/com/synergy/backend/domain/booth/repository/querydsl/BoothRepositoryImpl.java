package com.synergy.backend.domain.booth.repository.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.synergy.backend.domain.booth.dto.boothParticipateDto.BoothParticipateInterestedTechDto;
import com.synergy.backend.domain.booth.dto.boothParticipateDto.BoothParticipationInterestedResponseDto;
import com.synergy.backend.domain.booth.dto.QBoothParticipateInterestedTechDto;
import com.synergy.backend.domain.booth.dto.QBoothParticipationResponseDto;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.synergy.backend.domain.booth.entity.QBooth.booth;
import static com.synergy.backend.domain.booth.entity.QBoothParticipation.boothParticipation;
import static com.synergy.backend.domain.conference.entity.QConference.conference;
import static com.synergy.backend.domain.interest.entity.QAttendeeInterest.attendeeInterest;
import static com.synergy.backend.domain.interest.entity.QInterest.interest;
import static com.synergy.backend.domain.member.entity.QAttendee.attendee;

@RequiredArgsConstructor
public class BoothRepositoryImpl implements BoothRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BoothParticipationInterestedResponseDto> searchBoothParticipation(Long conferenceId) {
        List<BoothParticipationInterestedResponseDto> booths = queryFactory
                .select(new QBoothParticipationResponseDto(
                        booth.id,
                        booth.companyName,
                        booth.companyType,
                        booth.boothLocation,
                        booth.boothNumber,
                        booth.progressDate
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
