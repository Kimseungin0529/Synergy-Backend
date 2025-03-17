package com.synergy.backend.domain.member.exception;

import static com.synergy.backend.domain.member.exception.ErrorType.*;

import com.synergy.backend.global.exception.BaseErrorException;

public class NotFoundRecruiterException extends BaseErrorException {

	public NotFoundRecruiterException() {
		super(_RECRUITER_NOT_FOUND.getCode(), _RECRUITER_NOT_FOUND.getMessage());
	}
}
