package com.synergy.backend.domain.member.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.synergy.backend.domain.member.api.dto.AttendeeFilterRequest;
import com.synergy.backend.domain.member.api.dto.resposne.AttendeeSimpleResponseDto;
import com.synergy.backend.domain.member.api.dto.resposne.QAttendeeSimpleResponseDto;
import com.synergy.backend.domain.member.entity.details.AgeGroup;
import com.synergy.backend.domain.member.entity.details.EducationLevelType;
import com.synergy.backend.domain.member.entity.details.ExperienceLevelType;
import com.synergy.backend.domain.member.entity.details.RegionType;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.synergy.backend.domain.job.QJobPosition.jobPosition;
import static com.synergy.backend.domain.member.entity.QAttendee.attendee;
import static com.synergy.backend.domain.member.entity.QRecruiterAttendeeLike.recruiterAttendeeLike;

@RequiredArgsConstructor
public class AttendeeRepositoryImpl implements AttendeeRepositoryCustom {

    private static final Logger log = LoggerFactory.getLogger(AttendeeRepositoryImpl.class);
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AttendeeSimpleResponseDto> searchPageAttendeesBy(Pageable pageable, Long recruiterId,
                                                                 AttendeeFilterRequest requestCondition) {

        List<AttendeeSimpleResponseDto> content = queryFactory
                .select(
                        new QAttendeeSimpleResponseDto(
                                attendee.id,
                                attendee.name,
                                attendee.profilePhotoUrl,
                                jobPosition.name,
                                attendee.experienceLevel,
                                attendee.techStacks,
                                new CaseBuilder()
                                        .when(recruiterAttendeeLike.id.isNotNull()).then(true)
                                        .otherwise(false)
                        )
                ).from(attendee)
                .leftJoin(recruiterAttendeeLike)
                .on(recruiterAttendeeLike.attendee.eq(attendee)
                        .and(recruiterAttendeeLike.recruiter.id.eq(recruiterId))) // 현재 로그인 리크루터의 좋아요 여부
                .join(attendee.desiredJobPosition, jobPosition)
                .where(
                        occupationIn(requestCondition.desiredOccupations()),
                        educationEq(requestCondition.educationLevel()),
                        ageGroupEq(requestCondition.ageGroup()),
                        experienceEq(requestCondition.experienceLevel()),
                        desiredRegionIn(requestCondition.regions()),
                        attendee.isHiringInterested.eq(true)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, content.size());
    }

    private BooleanExpression occupationIn(List<String> occupations) {
        return (occupations == null || occupations.isEmpty()) ? null : attendee.currentJobPosition.name.in(occupations);

    }

    private BooleanExpression educationEq(String educationLevel) {

        return Arrays.stream(EducationLevelType.values())
                .filter(e -> e.getDescription().equals(educationLevel))
                .findFirst()
                .map(attendee.educationLevel::eq)
                .orElse(null);
    }

    private BooleanExpression ageGroupEq(String ageGroup) {

        return Arrays.stream(AgeGroup.values())
                .filter(e -> e.getDescription().equals(ageGroup))
                .findFirst()
                .map(attendee.ageGroup::eq)
                .orElse(null);
    }

    private BooleanExpression experienceEq(String experienceLevel) {
        return Arrays.stream(ExperienceLevelType.values())
                .filter(e -> e.getDescription().equals(experienceLevel))
                .findFirst()
                .map(attendee.experienceLevel::eq)
                .orElse(null);
    }

    private BooleanExpression desiredRegionIn(List<String> regions) {
        if (regions == null || regions.isEmpty())
            return null;

        List<RegionType> types = Arrays.stream(RegionType.values())
                .filter(r -> regions.contains(r.getDescription()))
                .collect(Collectors.toList());

        return types.isEmpty() ? null : attendee.desiredWorkRegion.any().in(types);
    }
}
