package com.synergy.backend.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorType {

    _NOT_AUTHORIZED(401, "접근할 권한이 없습니다.");

    private final int code;
    private final String message;
}
