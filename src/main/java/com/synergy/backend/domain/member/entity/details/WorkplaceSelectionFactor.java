package com.synergy.backend.domain.member.entity.details;

import java.util.function.Supplier;

import com.synergy.backend.domain.member.exception.InvalidWorkplaceSelectionFactorCodeException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WorkplaceSelectionFactor implements BaseAttendeeDetailEnum {
	GROWTH_AND_LEARNING_SUPPORT(1, "성장 기회 및 학습 지원"),
	SALARY_AND_BENEFITS(2, "연봉 및 복리후생"),
	WORK_LIFE_BALANCE(3, "워라밸 (Work-Life Balance)"),
	PROJECT_SCALE_AND_CHALLENGE(4, "프로젝트의 규모와 도전성"),
	COMPANY_STABILITY_AND_VISION(5, "회사의 안정성 및 비전"),
	;

	private final Integer code;
	private final String description;
	private final Supplier<? extends RuntimeException> exceptionSupplier = InvalidWorkplaceSelectionFactorCodeException::new;

	public static WorkplaceSelectionFactor fromCode(int code) {
		return BaseAttendeeDetailEnum.fromCode(WorkplaceSelectionFactor.class, code);
	}
}
