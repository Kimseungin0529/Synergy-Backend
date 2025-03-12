package com.synergy.backend.global.util;

import com.synergy.backend.global.exception.BaseErrorException;

import static com.synergy.backend.global.util.ErrorType._EXTERNAL_API_FAIL_CALL;

public class ExternalApiCallException extends BaseErrorException {

    public ExternalApiCallException(String errorMessage) {
        super(_EXTERNAL_API_FAIL_CALL.getCode(), errorMessage);
    }
}

