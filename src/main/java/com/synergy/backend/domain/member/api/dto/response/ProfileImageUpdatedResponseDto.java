package com.synergy.backend.domain.member.api.dto.response;

public record ProfileImageUpdatedResponseDto(String profileImageUrl) {
	public static ProfileImageUpdatedResponseDto from(String profileImageUrl) {
		return new ProfileImageUpdatedResponseDto(profileImageUrl);
	}
}
