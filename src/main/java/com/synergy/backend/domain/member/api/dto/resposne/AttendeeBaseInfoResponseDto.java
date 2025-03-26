package com.synergy.backend.domain.member.api.dto.resposne;

import com.synergy.backend.domain.member.entity.Attendee;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "참가자 기본 정보 응답 DTO")
public record AttendeeBaseInfoResponseDto(

	@Schema(description = "이름", example = "김지원")
	String name,

	@Schema(description = "희망 직무", example = "백엔드 개발")
	String jobName,

	@Schema(description = "경력", example = "1~2년 이하")
	String experience,

	@Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg")
	String profileImg
) {
	public static AttendeeBaseInfoResponseDto from(Attendee attendee) {
		return new AttendeeBaseInfoResponseDto(
			attendee.getName(),
			attendee.getCurrentJobPosition() != null ? attendee.getCurrentJobPosition().getName() : "",
			attendee.getExperienceLevel() != null ? attendee.getExperienceLevel().getDescription() : "",
			attendee.getProfileImageUrl()
		);
	}
}
