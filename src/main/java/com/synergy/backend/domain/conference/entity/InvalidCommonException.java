package com.synergy.backend.domain.conference.entity;

import com.synergy.backend.global.exception.BaseErrorException;

import static com.synergy.backend.domain.conference.exception.ErrorType._INVALID_COMMON;

public class InvalidCommonException extends BaseErrorException {
    public InvalidCommonException() {
        super(_INVALID_COMMON.getCode(), _INVALID_COMMON.getMessage());
    }
}
