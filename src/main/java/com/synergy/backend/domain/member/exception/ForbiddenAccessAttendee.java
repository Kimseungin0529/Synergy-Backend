package com.synergy.backend.domain.member.exception;

import com.synergy.backend.global.exception.BaseErrorException;

import static com.synergy.backend.domain.member.exception.ErrorType.*;

public class ForbiddenAccessAttendee extends BaseErrorException {
    public ForbiddenAccessAttendee() {
        super(_FORBIDDEN_ACCESS_ATTENDEE.getCode(), _FORBIDDEN_ACCESS_ATTENDEE.getMessage());
    }
}
