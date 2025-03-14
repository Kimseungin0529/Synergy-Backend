package com.synergy.backend.domain.member.exception;

import static com.synergy.backend.domain.member.exception.ErrorType.*;

import com.synergy.backend.global.exception.BaseErrorException;

public class NotFoundUserException extends BaseErrorException {

	public NotFoundUserException() {
		super(_USER_NOT_FOUND.getCode(), _USER_NOT_FOUND.getMessage());
	}
}
