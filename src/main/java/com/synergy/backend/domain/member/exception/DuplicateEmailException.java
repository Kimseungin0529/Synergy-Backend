package com.synergy.backend.domain.member.exception;

import static com.synergy.backend.domain.member.exception.ErrorType.*;

import com.synergy.backend.global.exception.BaseErrorException;

public class DuplicateEmailException extends BaseErrorException {

	public DuplicateEmailException() {
		super(_DUPLICATE_EMAIL_EXCEPTION.getCode(), _DUPLICATE_EMAIL_EXCEPTION.getMessage());
	}
}
