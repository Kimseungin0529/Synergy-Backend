package com.synergy.backend.domain.member.exception;

import static com.synergy.backend.domain.member.exception.ErrorType.*;

import com.synergy.backend.global.exception.BaseErrorException;

public class InvalidAgeGroupCodeException extends BaseErrorException {
	public InvalidAgeGroupCodeException() {
		super(_INVALID_AGE_GROUP_CODE.getCode(), _INVALID_AGE_GROUP_CODE.getMessage());

	}
}
