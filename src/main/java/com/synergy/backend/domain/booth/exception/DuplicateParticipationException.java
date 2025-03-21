package com.synergy.backend.domain.booth.exception;

import com.synergy.backend.global.exception.BaseErrorException;

import static com.synergy.backend.domain.booth.exception.ErrorType._DUPLICATE_PARTICIPATION;

public class DuplicateParticipationException extends BaseErrorException {
    public DuplicateParticipationException() {
        super(_DUPLICATE_PARTICIPATION.getCode(), _DUPLICATE_PARTICIPATION.getMessage());
    }

    public DuplicateParticipationException(String message) {
        super(_DUPLICATE_PARTICIPATION.getCode(), message);
    }
}
