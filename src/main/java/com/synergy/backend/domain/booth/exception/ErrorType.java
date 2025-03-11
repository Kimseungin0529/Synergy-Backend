package com.synergy.backend.domain.booth.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorType {

    _NOT_FOUND_BOOTH(400, "해당 부스를 찾을 수 없습니다.");

    private final int code;
    private final String message;
}
