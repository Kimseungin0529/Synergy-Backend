package com.synergy.backend.domain.member.api.dto.resposne;

import com.synergy.backend.domain.member.entity.Attendee;

public record LikedAttendeeResponseDto(
	Long attendeeId,
	String name,
	String desiredJobPosition,
	String techStacks,
	String experienceLevel,
	String profilePhotoUrl
) {
	public static LikedAttendeeResponseDto from(Attendee attendee) {
		return new LikedAttendeeResponseDto(
			attendee.getId(),
			attendee.getName(),
			attendee.getDesiredJobPosition() != null ? attendee.getDesiredJobPosition().getName() : "",
			attendee.getTechStacks(),
			attendee.getExperienceLevel() != null ? attendee.getExperienceLevel().getDescription() : "",
			attendee.getProfilePhotoUrl()
		);
	}
}
