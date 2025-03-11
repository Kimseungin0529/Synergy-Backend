package com.synergy.backend.domain.qrCode.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorType {

    _NOT_CREATE_QRCode(400, "QR코드를 생성할 수 없습니다.");

    private final int code;
    private final String message;
}
