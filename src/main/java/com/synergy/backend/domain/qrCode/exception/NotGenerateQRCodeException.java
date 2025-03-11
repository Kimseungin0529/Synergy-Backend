package com.synergy.backend.domain.qrCode.exception;

import com.synergy.backend.global.exception.BaseErrorException;

import static com.synergy.backend.domain.qrCode.exception.ErrorType._NOT_CREATE_QRCode;

public class NotGenerateQRCodeException extends BaseErrorException {
    public NotGenerateQRCodeException() {
        super(_NOT_CREATE_QRCode.getCode(), _NOT_CREATE_QRCode.getMessage());
    }
}
