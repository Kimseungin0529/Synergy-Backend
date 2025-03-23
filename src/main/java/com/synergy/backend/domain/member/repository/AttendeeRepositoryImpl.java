package com.synergy.backend.domain.member.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.synergy.backend.domain.member.api.dto.AttendeeFilterRequest;
import com.synergy.backend.domain.member.api.dto.AttendeeSimpleResponseDto;
import com.synergy.backend.domain.member.api.dto.QAttendeeSimpleResponseDto;
import com.synergy.backend.domain.member.entity.details.AgeGroup;
import com.synergy.backend.domain.member.entity.details.EducationLevelType;
import com.synergy.backend.domain.member.entity.details.ExperienceLevelType;
import com.synergy.backend.domain.member.entity.details.RegionType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.synergy.backend.domain.job.QOccupationCategory.occupationCategory;
import static com.synergy.backend.domain.member.entity.QAttendee.attendee;


@RequiredArgsConstructor
public class AttendeeRepositoryImpl implements AttendeeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AttendeeSimpleResponseDto> searchPageAttendeesBy(Pageable pageable, AttendeeFilterRequest requestCondition) {

        List<AttendeeSimpleResponseDto> content = queryFactory
                .select(
                        new QAttendeeSimpleResponseDto(
                        attendee.name,
                        attendee.profilePhotoUrl,
                        occupationCategory.name,
                        attendee.experienceLevel,
                        attendee.techStacks
                        )
                ).from(attendee)
                .join(attendee.currentOccupationCategory, occupationCategory)
                //.fetchJoin()
                .where(
                        occupationIn(requestCondition.occupations()),
                        educationEq(requestCondition.educationLevel()),
                        ageGroupEq(requestCondition.ageGroup()),
                        experienceEq(requestCondition.experienceLevel()),
                        desiredRegionIn(requestCondition.regions())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, content.size());
    }


    private BooleanExpression occupationIn(List<String> occupations) {
        return (occupations == null || occupations.isEmpty())
                ? null
                : attendee.currentOccupationCategory.name.in(occupations);

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
        if (regions == null || regions.isEmpty()) return null;

        List<RegionType> types = Arrays.stream(RegionType.values())
                .filter(r -> regions.contains(r.getDescription()))
                .collect(Collectors.toList());

        return types.isEmpty() ? null : attendee.desiredWorkRegion.any().in(types);
    }
}
