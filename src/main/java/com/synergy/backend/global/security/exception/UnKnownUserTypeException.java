package com.synergy.backend.global.security.exception;

import static com.synergy.backend.global.security.exception.ErrorType.*;

import com.synergy.backend.global.exception.BaseErrorException;

public class UnKnownUserTypeException extends BaseErrorException {
	public UnKnownUserTypeException() {
      super(_UNKNOWN_USER_TYPE.getCode(), _UNKNOWN_USER_TYPE.getMessage());
	}
}
