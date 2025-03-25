package com.synergy.backend.domain.member.repository;

import com.synergy.backend.domain.member.api.dto.AttendeeFilterRequest;
import com.synergy.backend.domain.member.api.dto.resposne.AttendeeSimpleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface AttendeeRepositoryCustom {
    Page<AttendeeSimpleResponseDto> searchPageAttendeesBy(Pageable pageable, Long recruiter, AttendeeFilterRequest requestCondition);
}
