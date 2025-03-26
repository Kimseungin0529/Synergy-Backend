package com.synergy.backend.domain.meta.enums;

import com.synergy.backend.domain.member.entity.details.BaseAttendeeDetailEnum;

public record EnumResponseDto(
	Integer code,
	String description
) {
	public static EnumResponseDto from(BaseAttendeeDetailEnum e) {
		return new EnumResponseDto(e.getCode(), e.getDescription());
	}
}
