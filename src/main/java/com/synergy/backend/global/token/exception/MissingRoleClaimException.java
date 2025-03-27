package com.synergy.backend.global.token.exception;

import static com.synergy.backend.global.token.exception.ErrorType.*;

import com.synergy.backend.global.exception.BaseErrorException;

public class MissingRoleClaimException extends BaseErrorException {
	public MissingRoleClaimException() {
		super(_MISSING_ROLE_CLAIM.getCode(), _MISSING_ROLE_CLAIM.getMessage());
	}
}
