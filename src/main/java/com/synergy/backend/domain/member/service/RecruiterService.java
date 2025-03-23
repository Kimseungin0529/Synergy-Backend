package com.synergy.backend.domain.member.service;

import com.synergy.backend.domain.member.api.dto.AttendeeFilterRequest;
import com.synergy.backend.domain.member.api.dto.AttendeeListResponse;
import com.synergy.backend.domain.member.api.dto.resposne.RecruiterMyInfoResponseDto;
import org.springframework.data.domain.Pageable;

public interface RecruiterService {
	RecruiterMyInfoResponseDto getMyInformation(Long id);

	AttendeeDetailResponse getAttendeeFrom(Long attendeeId);

	AttendeeListResponse getAttendeesBy(Pageable pageable, Long recruiterId, AttendeeFilterRequest request);
}
