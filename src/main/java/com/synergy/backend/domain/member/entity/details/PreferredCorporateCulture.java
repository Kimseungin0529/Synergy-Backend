package com.synergy.backend.domain.member.entity.details;

import java.util.function.Supplier;

import com.synergy.backend.domain.member.exception.InvalidPreferredCorporateCultureCodeException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PreferredCorporateCulture implements BaseAttendeeDetailEnum {
	HORIZONTAL_COMMUNICATION(1, "수평적 소통 문화"),
	CONTINUOUS_TECH_INNOVATION(2, "지속적인 기술 혁신 추구"),
	COLLABORATION_AND_KNOWLEDGE_SHARING(3, "협업과 지식 공유 중시"),
	FLEXIBLE_WORK_ENVIRONMENT(4, "유연한 근무 환경"),
	PERFORMANCE_BASED_EVALUATION(5, "성과 중심 평가"),
	;

	private final Integer code;
	private final String description;
	private final Supplier<? extends RuntimeException> exceptionSupplier = InvalidPreferredCorporateCultureCodeException::new;

	public static PreferredCorporateCulture fromCode(int code) {
		return BaseAttendeeDetailEnum.fromCode(PreferredCorporateCulture.class, code);
	}
}
