package com.synergy.backend.domain.member.api.dto.resposne;

import com.synergy.backend.domain.member.entity.Attendee;

public record AttendeeBaseInfoResponseDto(
	String name,
	String jobName,
	String experience,
	String profilePhotoUrl
) {
	public static AttendeeBaseInfoResponseDto from(Attendee attendee) {
		return new AttendeeBaseInfoResponseDto(
			attendee.getName(),
			attendee.getCurrentJobCategory() != null ? attendee.getCurrentJobCategory().getName() : "",
			attendee.getExperienceLevel() != null ? attendee.getExperienceLevel().getDescription() : "",
			attendee.getProfilePhotoUrl()
		);
	}
}
