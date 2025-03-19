package com.synergy.backend.domain.member.entity.details;

import java.util.function.Supplier;

import com.synergy.backend.domain.member.exception.InvalidEducationLevelTypeException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EducationLevelType implements BaseAttendeeDetailEnum {
	HIGH_SCHOOL(1, "고등학교 졸업"),
	ASSOCIATE(2, "2~3년제 졸업"),
	BACHELOR(3, "4년제 졸업"),
	GRADUATE(4, "대학원 석/박사");

	private final Integer code;
	private final String description;
	private final Supplier<? extends RuntimeException> exceptionSupplier = InvalidEducationLevelTypeException::new;

	public static EducationLevelType fromCode(int code) {
		return BaseAttendeeDetailEnum.fromCode(EducationLevelType.class, code);
	}
}
