package com.synergy.backend.global.security.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorType {

	_UNKNOWN_USER_TYPE(400, "존재하지 않는 사용자입니다."),
	_INVALID_CREDENTIALS(401, "잘못된 인증 정보입니다."),
	_ACCESS_DENIED(403, "접근이 거부되었습니다.");
	;

	private final int code;
	private final String message;
}
