package com.synergy.backend.domain.member.entity.details;

import java.util.Arrays;
import java.util.function.Supplier;

public interface BaseAttendeeDetailEnum {
	static <E extends Enum<E> & BaseAttendeeDetailEnum> E fromCode(Class<E> enumType, int code) {
		return Arrays.stream(enumType.getEnumConstants())
			.filter(e -> e.getCode() == code)
			.findFirst()
			.orElseThrow(() -> enumType.getEnumConstants()[0].getExceptionSupplier().get());
	}

	Integer getCode();

	Supplier<? extends RuntimeException> getExceptionSupplier();
}
