package com.synergy.backend.domain.interest.exception;

import static com.synergy.backend.domain.interest.exception.ErrorType.*;

import com.synergy.backend.global.exception.BaseErrorException;

public class NotFoundInterestException extends BaseErrorException {
	public NotFoundInterestException() {
		super(_INTEREST_NOT_FOUND.getCode(), _INTEREST_NOT_FOUND.getMessage());
	}
	public NotFoundInterestException(String message) {
		super(_INTEREST_NOT_FOUND.getCode(),  _INTEREST_NOT_FOUND.getMessage() + message);
	}
}
