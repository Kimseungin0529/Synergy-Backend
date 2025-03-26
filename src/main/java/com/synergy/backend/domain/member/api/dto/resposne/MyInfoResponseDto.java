package com.synergy.backend.domain.member.api.dto.resposne;

import com.synergy.backend.domain.member.entity.Attendee;

public record MyInfoResponseDto(
	Long attendeeId,
	String name,
	String membershipLevel,
	Integer totalPoints,
	Boolean isHiringInterested,
	String profileImg,
	NextPointResponseDto nextPointResponseDto
) {
	public static MyInfoResponseDto from(Attendee attendee, NextPointResponseDto nextPointResponseDto) {
		return new MyInfoResponseDto(
			attendee.getId(),
			attendee.getName(),
			attendee.getMembershipLevelType().name(),
			attendee.getTotalPoints(),
			attendee.getIsHiringInterested(),
			attendee.getProfileImageUrl(),
			nextPointResponseDto
		);
	}
}
