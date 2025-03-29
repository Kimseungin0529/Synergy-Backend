package com.synergy.backend.domain.member.api.dto.resposne;

public record ProfileImageUpdatedResponseDto(String profileImageUrl) {
	public static ProfileImageUpdatedResponseDto from(String profileImageUrl) {
		return new ProfileImageUpdatedResponseDto(profileImageUrl);
	}
}
