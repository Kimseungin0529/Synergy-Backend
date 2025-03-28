package com.synergy.backend.global.token.exception;

import static com.synergy.backend.global.token.exception.ErrorType.*;

import com.synergy.backend.global.exception.BaseErrorException;

public class ExpiredRefreshTokenException extends BaseErrorException {
	public ExpiredRefreshTokenException() {
		super(_EXPIRED_REFRESH_TOKEN.getCode(), _EXPIRED_REFRESH_TOKEN.getMessage());
	}
}
