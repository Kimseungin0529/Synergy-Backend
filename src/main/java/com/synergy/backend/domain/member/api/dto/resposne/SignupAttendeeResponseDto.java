package com.synergy.backend.domain.member.api.dto.resposne;

import com.synergy.backend.domain.member.entity.Attendee;

public record SignupAttendeeResponseDto(
	String name,
	String email
) {
	public static SignupAttendeeResponseDto from(Attendee attendee) {
		return new SignupAttendeeResponseDto(attendee.getName(), attendee.getEmail());
	}
}
