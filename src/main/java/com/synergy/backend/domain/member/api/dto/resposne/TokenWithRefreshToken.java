package com.synergy.backend.domain.member.api.dto.resposne;

public record TokenWithRefreshToken(
	String refreshToken,
	TokenResponseDto tokenResponseDto
) {
	public static TokenWithRefreshToken of(String refreshToken, TokenResponseDto dto) {
		return new TokenWithRefreshToken(refreshToken, dto);
	}
}
