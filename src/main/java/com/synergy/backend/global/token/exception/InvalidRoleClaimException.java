package com.synergy.backend.global.token.exception;

import static com.synergy.backend.global.token.exception.ErrorType.*;

import io.jsonwebtoken.JwtException;

public class InvalidRoleClaimException extends JwtException {
	public InvalidRoleClaimException() {
		super(_INVALID_ROLE_CLAIM.getMessage());
	}
}
