package com.synergy.backend.domain.point.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public enum ErrorType {
	_POINT_NOT_FOUND(400, "포인트를 찾을 수 없습니다."),;

	private final int code;
	private final String message;
}
