package com.synergy.backend.domain.session.exception;

import com.synergy.backend.global.exception.BaseErrorException;

import static com.synergy.backend.domain.session.exception.ErrorType.ALREADY_ATTENDED;

public class AlreadyAttendedException extends BaseErrorException {
    public AlreadyAttendedException() {
        super(ALREADY_ATTENDED.getCode(), ALREADY_ATTENDED.getMessage());
    }
}
