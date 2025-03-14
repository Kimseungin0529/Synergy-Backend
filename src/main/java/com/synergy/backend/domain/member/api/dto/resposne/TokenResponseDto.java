package com.synergy.backend.domain.member.api.dto.resposne;

public record TokenResponseDto(
	String accessToken,
	// String refreshToken,
	String identifier,
	String role) {
}
