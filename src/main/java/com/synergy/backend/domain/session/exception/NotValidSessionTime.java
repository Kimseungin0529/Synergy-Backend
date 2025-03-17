package com.synergy.backend.domain.session.exception;

import com.synergy.backend.global.exception.BaseErrorException;

import static com.synergy.backend.domain.session.exception.ErrorType.NOT_VALID_SESSION_TIME;

public class NotValidSessionTime extends BaseErrorException {
    public NotValidSessionTime() {
        super(NOT_VALID_SESSION_TIME.getCode(), NOT_VALID_SESSION_TIME.getMessage());
    }
}
