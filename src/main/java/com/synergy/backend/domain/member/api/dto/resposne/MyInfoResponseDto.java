package com.synergy.backend.domain.member.api.dto.resposne;

import java.util.List;

import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.point.api.dto.PointResponseDto;

public record MyInfoResponseDto(
	String name,
	String membershipLevel,
	Integer totalPoints,
	List<PointResponseDto> recentPoints,
	Boolean isHiringInterested
) {
	public static MyInfoResponseDto from(Attendee attendee, List<PointResponseDto> recentPoints) {
		return new MyInfoResponseDto(
			attendee.getName(),
			attendee.getMembershipLevelType().name(),
			attendee.getTotalPoints(),
			recentPoints,
			attendee.getIsHiringInterested()
		);
	}
}
