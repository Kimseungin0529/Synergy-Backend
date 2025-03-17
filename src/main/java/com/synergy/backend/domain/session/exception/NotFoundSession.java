package com.synergy.backend.domain.session.exception;

import com.synergy.backend.global.exception.BaseErrorException;

import static com.synergy.backend.domain.session.exception.ErrorType.NOT_FOUND_SESSION;

public class NotFoundSession extends BaseErrorException {


    public NotFoundSession() {
        super(NOT_FOUND_SESSION.getCode(), NOT_FOUND_SESSION.getMessage());
    }
}
