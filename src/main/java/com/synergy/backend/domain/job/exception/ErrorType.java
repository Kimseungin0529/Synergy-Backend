package com.synergy.backend.domain.job.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorType {

	_JOB_POSITION_NOT_FOUND(400, "해당 직무를 찾을 수 없습니다."),
	_JOB_GROUP_NOT_FOUND(400, "해당 직군을 찾을 수 없습니다."),
	;

	private final int code;
	private final String message;
}
