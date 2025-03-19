package com.synergy.backend.domain.member.entity.details;

import java.util.function.Supplier;

import com.synergy.backend.domain.member.exception.InvalidExperienceLevelTypeException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExperienceLevelType implements BaseAttendeeDetailEnum {
	NEWCOMER(1, "신입"),
	JUNIOR(2, "1~2년 이하"),
	MID_LEVEL(3, "3~4년 이하"),
	SENIOR(4, "5년 이상");

	private final Integer code;
	private final String description;
	private final Supplier<? extends RuntimeException> exceptionSupplier = InvalidExperienceLevelTypeException::new;

	public static ExperienceLevelType fromCode(int code) {
		return BaseAttendeeDetailEnum.fromCode(ExperienceLevelType.class, code);
	}
}
