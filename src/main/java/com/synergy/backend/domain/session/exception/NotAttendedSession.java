package com.synergy.backend.domain.session.exception;

import com.synergy.backend.global.exception.BaseErrorException;

import static com.synergy.backend.domain.session.exception.ErrorType._NOT_ATTENDED_SESSION;

public class NotAttendedSession extends BaseErrorException {
    public NotAttendedSession() {
        super(_NOT_ATTENDED_SESSION.getCode(), _NOT_ATTENDED_SESSION.getMessage());
    }
}
