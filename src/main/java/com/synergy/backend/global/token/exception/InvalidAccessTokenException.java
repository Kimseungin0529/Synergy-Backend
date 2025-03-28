package com.synergy.backend.global.token.exception;

import static com.synergy.backend.global.token.exception.ErrorType.*;

import io.jsonwebtoken.JwtException;

public class InvalidAccessTokenException extends JwtException {
	public InvalidAccessTokenException() {
		super(_INVALID_ACCESS_TOKEN.getMessage());
	}
}
