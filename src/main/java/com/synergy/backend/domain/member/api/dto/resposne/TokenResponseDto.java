package com.synergy.backend.domain.member.api.dto.resposne;

import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.domain.member.entity.User;

public record TokenResponseDto(
	String accessToken,
	String identifier,
	RoleType role,
	Long id) {

	public static TokenResponseDto of(String token, User user) {
		return new TokenResponseDto(
			token,
			user.getIdentifier(),
			user.getRole(),
			user.getId()
		);
	}
}
