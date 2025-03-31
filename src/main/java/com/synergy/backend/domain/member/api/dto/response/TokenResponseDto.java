package com.synergy.backend.domain.member.api.dto.response;

import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.domain.member.entity.User;

public record TokenResponseDto(
	String accessToken,
	String identifier,
	RoleType role,
	Long id) {

	public static TokenResponseDto from(String token, User user) {
		return new TokenResponseDto(
			token,
			user.getIdentifier(),
			user.getRole(),
			user.getId()
		);
	}
}
