package com.synergy.backend.domain.member.exception;

import static com.synergy.backend.domain.member.exception.ErrorType.*;

import com.synergy.backend.global.exception.BaseErrorException;

public class AdminOrRecruiterNotFoundException extends BaseErrorException {
	public AdminOrRecruiterNotFoundException() {
		super(_ADMIN_OR_RECRUITER_NOT_FOUND.getCode(), _ADMIN_OR_RECRUITER_NOT_FOUND.getMessage());
	}
}
