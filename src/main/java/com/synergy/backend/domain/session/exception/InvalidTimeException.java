package com.synergy.backend.domain.session.exception;

import com.synergy.backend.global.exception.BaseErrorException;

import static com.synergy.backend.domain.session.exception.ErrorType.INVALID_TIME;

public class InvalidTimeException extends BaseErrorException {
    public InvalidTimeException() {
        super(INVALID_TIME.getCode(), INVALID_TIME.getMessage());
    }
}
