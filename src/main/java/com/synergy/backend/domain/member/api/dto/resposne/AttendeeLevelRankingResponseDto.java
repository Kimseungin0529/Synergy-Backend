package com.synergy.backend.domain.member.api.dto.resposne;

import com.synergy.backend.domain.member.entity.Attendee;

public record AttendeeLevelRankingResponseDto(
	String membershipLevel,
	String attendeeName,
	Integer totalPoints,
	Long userId
) {
	public static AttendeeLevelRankingResponseDto from(Attendee attendee) {
		return new AttendeeLevelRankingResponseDto(
			attendee.getMembershipLevelType().name(),
			attendee.getName(),
			attendee.getTotalPoints(),
			attendee.getId()
		);
	}
}
