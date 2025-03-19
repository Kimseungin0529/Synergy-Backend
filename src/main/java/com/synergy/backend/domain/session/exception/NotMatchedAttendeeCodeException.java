package com.synergy.backend.domain.session.exception;

import com.synergy.backend.global.exception.BaseErrorException;

import static com.synergy.backend.domain.session.exception.ErrorType.NOT_MATCHED_ATTENDEE_CODE;

public class NotMatchedAttendeeCodeException extends BaseErrorException {
    public NotMatchedAttendeeCodeException() {
        super(NOT_MATCHED_ATTENDEE_CODE.getCode(), NOT_MATCHED_ATTENDEE_CODE.getMessage());
    }
}
