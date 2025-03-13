package com.synergy.backend.domain.session.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorType {

    _NOT_FOUND_SESSION(400, "해당 세션을 찾을 수 없습니다."),
    _NOT_VALID_SESSION_TIME(400, "알맞은 시간 형식이 아닙니다."),
    _NOT_ATTENDED_SESSION(401, "세션에 참여한 사용자가 아닙니다.");

    private final int code;
    private final String message;
}
