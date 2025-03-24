package com.synergy.backend.domain.member.exception;

import static com.synergy.backend.domain.member.exception.ErrorType.*;

import com.synergy.backend.global.exception.BaseErrorException;

public class InvalidAccountInformationException extends BaseErrorException {

	public InvalidAccountInformationException() {
		super(_INVALID_ACCOUNT_INFORMATION.getCode(), _INVALID_ACCOUNT_INFORMATION.getMessage());
	}
}
