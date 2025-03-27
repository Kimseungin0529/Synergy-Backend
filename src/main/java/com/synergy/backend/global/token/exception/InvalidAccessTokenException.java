package com.synergy.backend.global.token.exception;

import static com.synergy.backend.global.token.exception.ErrorType.*;

import com.synergy.backend.global.exception.BaseErrorException;

public class InvalidAccessTokenException extends BaseErrorException {
	public InvalidAccessTokenException() {
		super(_INVALID_ACCESS_TOKEN.getCode(), _INVALID_ACCESS_TOKEN.getMessage());
	}
}
