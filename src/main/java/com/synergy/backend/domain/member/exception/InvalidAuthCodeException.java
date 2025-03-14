package com.synergy.backend.domain.member.exception;

import static com.synergy.backend.domain.member.exception.ErrorType.*;

import com.synergy.backend.global.exception.BaseErrorException;

public class InvalidAuthCodeException extends BaseErrorException {
	public InvalidAuthCodeException() {
		super(_INVALID_AUTH_CODE.getCode(), _INVALID_AUTH_CODE.getMessage());

	}
}
