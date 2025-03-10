package com.synergy.backend.global.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorType {

    _FILE_UPLOAD_FAIL(400, "잘못된 파일 형식이 존재합니다.");

    private final int code;
    private final String message;
}
