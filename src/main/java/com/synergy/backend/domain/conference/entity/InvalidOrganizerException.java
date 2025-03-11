package com.synergy.backend.domain.conference.entity;

import com.synergy.backend.global.exception.BaseErrorException;

import static com.synergy.backend.domain.conference.exception.ErrorType._INVALID_ORGANIZER;

public class InvalidOrganizerException extends BaseErrorException {
    public InvalidOrganizerException() {
        super(_INVALID_ORGANIZER.getCode(), _INVALID_ORGANIZER.getMessage());
    }
}