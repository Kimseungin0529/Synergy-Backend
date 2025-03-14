package com.synergy.backend.domain.member.api.dto.request;

public record LoginAttendeeRequestDto(
	String email,
	String password
) {
}
