package com.synergy.backend.domain.member.service;

import com.synergy.backend.domain.member.api.dto.request.AttendeeFilterRequest;
import com.synergy.backend.domain.member.api.dto.response.AttendeeListResponse;
import com.synergy.backend.domain.member.api.dto.response.RecruiterMyInfoResponseDto;
import org.springframework.data.domain.Pageable;

public interface RecruiterService {
	RecruiterMyInfoResponseDto getMyInformation(Long id);

	AttendeeListResponse getAttendeesBy(Pageable pageable, Long recruiterId, AttendeeFilterRequest request);
}
