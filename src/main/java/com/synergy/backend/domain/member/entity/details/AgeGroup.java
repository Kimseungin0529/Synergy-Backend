package com.synergy.backend.domain.member.entity.details;

import java.util.function.Supplier;

import com.synergy.backend.domain.member.exception.InvalidAgeGroupCodeException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AgeGroup implements BaseAttendeeDetailEnum {
	AGE_20_24(20, "20~24세 이하"),
	AGE_25_29(25, "25~29세 이하"),
	AGE_30_34(30, "30~34세 이하"),
	AGE_35_PLUS(35, "35세 이상");

	private final Integer code;
	private final String description;
	private final Supplier<? extends RuntimeException> exceptionSupplier = InvalidAgeGroupCodeException::new;

	public static AgeGroup fromCode(int code) {
		return BaseAttendeeDetailEnum.fromCode(AgeGroup.class, code);
	}
}
