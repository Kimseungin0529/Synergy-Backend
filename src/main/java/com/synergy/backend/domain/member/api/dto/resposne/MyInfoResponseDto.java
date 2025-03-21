package com.synergy.backend.domain.member.api.dto.resposne;

import com.synergy.backend.domain.member.entity.Attendee;

public record MyInfoResponseDto(
	Long attendeeId,
	String name,
	String membershipLevel,
	Integer totalPoints,
	Boolean isHiringInterested
) {
	public static MyInfoResponseDto from(Attendee attendee) {
		return new MyInfoResponseDto(
			attendee.getId(),
			attendee.getName(),
			attendee.getMembershipLevelType().name(),
			attendee.getTotalPoints(),
			attendee.getIsHiringInterested()
		);
	}
}
