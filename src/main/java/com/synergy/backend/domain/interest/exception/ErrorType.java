package com.synergy.backend.domain.interest.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorType {

	_INTEREST_NOT_FOUND(400, "해당 관심사를 찾을 수 없습니다."),

	;

	private final int code;
	private final String message;
}
