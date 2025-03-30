package com.synergy.backend.domain.member.api.dto.resposne;

import com.synergy.backend.domain.member.entity.Attendee;

public record AttendeePointRankingResponseDto(
	Long attendeeId,
	String attendeeName,
	Integer totalPoints
) {
	public static AttendeePointRankingResponseDto from(Attendee attendee) {
		return new AttendeePointRankingResponseDto(
			attendee.getId(),
			attendee.getName(),
			attendee.getTotalPoints()
		);
	}
}
