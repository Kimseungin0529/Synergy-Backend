package com.synergy.backend.global.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorType {

    _EMAIL_NOT_VERIFIED(403, "이메일 인증이 완료되지 않았습니다.");

    private final int code;
    private final String message;
}
