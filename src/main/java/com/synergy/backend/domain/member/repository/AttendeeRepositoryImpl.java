package com.synergy.backend.domain.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.synergy.backend.domain.member.api.dto.AttendeeFilterRequest;
import com.synergy.backend.domain.member.api.dto.AttendeeSimpleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static com.synergy.backend.domain.member.entity.QAttendee.attendee;


@RequiredArgsConstructor
public class AttendeeRepositoryImpl implements AttendeeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AttendeeSimpleResponseDto> searchPageAttendeesBy(Pageable pageable, AttendeeFilterRequest requestCondition) {
        queryFactory
                .select(
//                        new AttendeeSimpleResponseDto(
//                        attendee.name,
//                        attendee.profilePhotoUrl,
//                        null,
//                        attendee.techStacks,
//                        attendee.experienceLevel
                )
                .from(attendee)
                .join(attendee.currentJobCategory, job).fetchJoin()

        return null;
    }
}
