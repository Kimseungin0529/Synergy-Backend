package com.synergy.backend.domain.member.service;

import com.synergy.backend.domain.member.api.dto.resposne.RecruiterMyInfoResponseDto;

public interface RecruiterService {
	RecruiterMyInfoResponseDto getMyInformation(Long id);

	AttendeeDetailResponse getAttendeeFrom(Long attendeeId);
}
