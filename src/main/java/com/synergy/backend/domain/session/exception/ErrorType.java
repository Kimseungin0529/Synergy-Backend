package com.synergy.backend.domain.session.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorType {

    INVALID_TIME(422, "금일에 열리는 세션들을 조회할 수 없습니다."),
    NOT_MATCHED_ATTENDEE_CODE(403, "해당 세션에 참여할 수 없습니다."),
    NOT_FOUND_SESSION(400, "해당 세션을 찾을 수 없습니다."),
    NOT_VALID_SESSION_TIME(400, "알맞은 시간 형식이 아닙니다."),
    NOT_ATTENDED_SESSION(401, "세션에 참여한 사용자가 아닙니다."),

    DECODING_ERROR(500, "디코딩 과정에 오류가 생겼습니다."),
    ALREADY_ATTENDED(409, "이미 세션에 참여한 사용자입니다.");


    private final int code;
    private final String message;
}
