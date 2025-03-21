package com.synergy.backend.domain.booth.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorType {

    _NOT_FOUND_BOOTH(404, "해당 부스를 찾을 수 없습니다."),
    _DUPLICATE_PARTICIPATION(409, "이미 참여한 부스입니다."),
    _NOT_FOUND_PARTICIPATION(404, "참여 내역이 없습니다.");

    private final int code;
    private final String message;
}
