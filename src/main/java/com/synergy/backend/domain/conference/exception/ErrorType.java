package com.synergy.backend.domain.conference.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorType {

    _INVALID_TIME_PERIOD(400, "시작 날짜는 더 빨라야 합니다."),
    _INVALID_CONFERENCE_NAME(400, "컨퍼런스명은 공백이나 30자를 넘어갈 수 없습니다."),
    _INVALID_COMMON(400, "컨퍼런스 유형은 50자를 넘어갈 수 없습니다."),
    _INVALID_ORGANIZER(400, "주최자 명은 10자를 넘어갈 수 없습니다."),
    _INVALID_CONFERENCE_LOCATION(400, "위치 정보는 공백이나 100자를 넘어갈 수 없습니다."),
    _NOT_FOUND_CONFERENCE(404, "해당 컨퍼런스를 찾을 수 없습니다.")
    ;

    private final int code;
    private final String message;
}
