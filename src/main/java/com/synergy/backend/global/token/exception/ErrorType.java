package com.synergy.backend.global.token.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorType {

	_INVALID_REFRESH_TOKEN(401, "유효하지 않은 리프레시 토큰입니다."),
	;

	private final int code;
	private final String message;
}
