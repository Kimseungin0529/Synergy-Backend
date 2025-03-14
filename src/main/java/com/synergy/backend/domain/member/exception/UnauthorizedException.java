package com.synergy.backend.domain.member.exception;

import static com.synergy.backend.domain.member.exception.ErrorType.*;

import com.synergy.backend.global.exception.BaseErrorException;

public class UnauthorizedException extends BaseErrorException {
	public UnauthorizedException() {
		super(_INVALID_EMAIL_OR_PASSWORD.getCode(), _INVALID_EMAIL_OR_PASSWORD.getMessage());
	}
}
