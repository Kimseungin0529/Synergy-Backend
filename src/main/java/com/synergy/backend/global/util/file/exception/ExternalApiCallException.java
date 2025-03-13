package com.synergy.backend.global.util.file.exception;

import com.synergy.backend.global.exception.BaseErrorException;

import static com.synergy.backend.global.util.file.exception.ErrorType._EXTERNAL_API_FAIL_CALL;

public class ExternalApiCallException extends BaseErrorException {

    public ExternalApiCallException(String errorMessage) {
        super(_EXTERNAL_API_FAIL_CALL.getCode(), errorMessage);
    }
}

