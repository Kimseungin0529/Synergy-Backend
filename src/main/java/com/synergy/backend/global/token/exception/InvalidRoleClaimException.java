package com.synergy.backend.global.token.exception;

import static com.synergy.backend.global.token.exception.ErrorType.*;

import com.synergy.backend.global.exception.BaseErrorException;

public class InvalidRoleClaimException extends BaseErrorException {
	public InvalidRoleClaimException() {
		super(_INVALID_ROLE_CLAIM.getCode(), _INVALID_ROLE_CLAIM.getMessage());
	}
}
