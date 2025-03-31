package com.synergy.backend.domain.member.api.dto.response;

import com.synergy.backend.domain.member.entity.Attendee;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "참가자 정보 상세보기 - 전체 정보 응답 DTO")
public record AttendeeFullInfoResponseDto(

	@Schema(description = "참가자 기본 정보")
	AttendeeBaseInfoResponseDto baseInfo,

	@Schema(description = "참가자 상세 정보")
	AttendeeDetailInfoResponseDto detailInfo
) {
	public static AttendeeFullInfoResponseDto from(Attendee attendee) {
		return new AttendeeFullInfoResponseDto(
			AttendeeBaseInfoResponseDto.from(attendee),
			AttendeeDetailInfoResponseDto.from(attendee)
		);
	}
}
