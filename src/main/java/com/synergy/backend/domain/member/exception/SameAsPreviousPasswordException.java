package com.synergy.backend.domain.member.exception;

import static com.synergy.backend.domain.member.exception.ErrorType.*;

import com.synergy.backend.global.exception.BaseErrorException;

public class SameAsPreviousPasswordException extends BaseErrorException {
	public SameAsPreviousPasswordException() {
		super(_SAME_AS_PREVIOUS_PASSWORD.getCode(), _SAME_AS_PREVIOUS_PASSWORD.getMessage());
	}
}
