package com.synergy.backend.domain.member.api.dto.resposne;

import com.synergy.backend.domain.member.entity.Attendee;

public record AttendeeFullInfoResponseDto(
	AttendeeBaseInfoResponseDto baseInfo,
	AttendeeDetailInfoResponseDto detailInfo
) {
	public static AttendeeFullInfoResponseDto from(Attendee attendee) {
		return new AttendeeFullInfoResponseDto(
			AttendeeBaseInfoResponseDto.from(attendee),
			AttendeeDetailInfoResponseDto.from(attendee)
		);
	}
}
