package com.synergy.backend.global.token.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorType {

	_INVALID_REFRESH_TOKEN(401, "유효하지 않은 리프레시 토큰입니다."),
	_INVALID_ACCESS_TOKEN(401, "유효하지 않은 액세스 토큰입니다."),
	_MISSING_ROLE_CLAIM(401, "토큰에 role 정보가 없습니다."),
	_INVALID_ROLE_CLAIM(401, "토큰의 role 값이 잘못되었습니다."),
	_EXPIRED_REFRESH_TOKEN(401, "리프레시 토큰이 만료되었습니다."),
	;

	private final int code;
	private final String message;
}
