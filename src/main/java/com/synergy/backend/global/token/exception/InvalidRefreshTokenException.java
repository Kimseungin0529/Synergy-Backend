package com.synergy.backend.global.token.exception;

import static com.synergy.backend.global.token.exception.ErrorType.*;

import com.synergy.backend.global.exception.BaseErrorException;

public class InvalidRefreshTokenException extends BaseErrorException {
	public InvalidRefreshTokenException() {
		super(_INVALID_REFRESH_TOKEN.getCode(), _INVALID_REFRESH_TOKEN.getMessage());
	}
}
