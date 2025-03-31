package com.synergy.backend.domain.member.api.dto.response;

import com.synergy.backend.domain.member.vo.NextPointInfo;

public record NextPointResponseDto(
	String nextMembershipLevel,
	Integer needPoint
) {
	public static NextPointResponseDto from(NextPointInfo info) {
		return new NextPointResponseDto(
			info.nextLevel().name(),
			info.needPoint()
		);
	}
}
