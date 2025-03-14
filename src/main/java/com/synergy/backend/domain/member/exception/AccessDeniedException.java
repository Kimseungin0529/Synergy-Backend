package com.synergy.backend.domain.member.exception;

import static com.synergy.backend.domain.member.exception.ErrorType.*;

import com.synergy.backend.global.exception.BaseErrorException;

public class AccessDeniedException extends BaseErrorException {

	public AccessDeniedException() {
		super(_ACCESS_DENIED.getCode(), _ACCESS_DENIED.getMessage());
	}

	public AccessDeniedException(String message) {
		super(_ACCESS_DENIED.getCode(), message);
	}
}
