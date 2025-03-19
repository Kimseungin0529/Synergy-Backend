package com.synergy.backend.domain.job.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorType {

	_JOB_CATEGORY_NOT_FOUND(400, "해당 직업 카테고리를 찾을 수 없습니다."),
	_OCCUPATION_CATEGORY_NOT_FOUND(400, "해당 직무 카테고리를 찾을 수 없습니다."),
	;

	private final int code;
	private final String message;
}
