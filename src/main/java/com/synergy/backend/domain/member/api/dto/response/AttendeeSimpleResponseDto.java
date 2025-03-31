package com.synergy.backend.domain.member.api.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.details.ExperienceLevelType;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AttendeeSimpleResponseDto {
	private Long attendeeId;
	private String name;
	private String desiredJobPosition;
	private String experienceLevel;
	private String techStacks;
	private String profileImageUrl;
	private boolean isLiked;

	@QueryProjection
	public AttendeeSimpleResponseDto(Long attendeeId, String name, String desiredJobPosition,
		ExperienceLevelType experienceLevel, String techStacks, String profileImageUrl, boolean isLiked) {
		this.attendeeId = attendeeId;
		this.name = name;
		this.desiredJobPosition = desiredJobPosition;
		this.experienceLevel = experienceLevel != null ? experienceLevel.getDescription() : null;
		this.techStacks = techStacks;
		this.profileImageUrl = profileImageUrl;
		this.isLiked = isLiked;
	}

	public static AttendeeSimpleResponseDto from(Attendee attendee, boolean isLiked) {
		return new AttendeeSimpleResponseDto(
			attendee.getId(),
			attendee.getName(),
			attendee.getDesiredJobPosition() != null ? attendee.getDesiredJobPosition().getName() : "",
			attendee.getExperienceLevel(),
			attendee.getTechStacks(),
			attendee.getProfileImageUrl(),
			isLiked
		);
	}
}
