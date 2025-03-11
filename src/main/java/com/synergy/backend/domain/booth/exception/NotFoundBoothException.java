package com.synergy.backend.domain.booth.exception;

import com.synergy.backend.global.exception.BaseErrorException;

import static com.synergy.backend.domain.booth.exception.ErrorType._NOT_FOUND_BOOTH;

public class NotFoundBoothException extends BaseErrorException {
    public NotFoundBoothException() {
        super(_NOT_FOUND_BOOTH.getCode(), _NOT_FOUND_BOOTH.getMessage());
    }

    public NotFoundBoothException(String message) {
        super(_NOT_FOUND_BOOTH.getCode(), message);
    }
}