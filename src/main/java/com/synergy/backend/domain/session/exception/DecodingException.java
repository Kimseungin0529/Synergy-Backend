package com.synergy.backend.domain.session.exception;

import com.synergy.backend.global.exception.BaseErrorException;

import static com.synergy.backend.domain.session.exception.ErrorType.DECODING_ERROR;


public class DecodingException extends BaseErrorException {
    public DecodingException() {
        super(DECODING_ERROR.getCode(), DECODING_ERROR.getMessage());
    }
}
