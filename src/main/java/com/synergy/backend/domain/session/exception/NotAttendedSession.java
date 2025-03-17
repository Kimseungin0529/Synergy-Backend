package com.synergy.backend.domain.session.exception;

import com.synergy.backend.global.exception.BaseErrorException;

import static com.synergy.backend.domain.session.exception.ErrorType.NOT_ATTENDED_SESSION;

public class NotAttendedSession extends BaseErrorException {
    public NotAttendedSession() {
        super(NOT_ATTENDED_SESSION.getCode(), NOT_ATTENDED_SESSION.getMessage());
    }
}
