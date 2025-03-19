package com.synergy.backend.domain.member.exception;

import static com.synergy.backend.domain.member.exception.ErrorType.*;

import com.synergy.backend.global.exception.BaseErrorException;

public class InvalidEducationLevelTypeException extends BaseErrorException {
	public InvalidEducationLevelTypeException() {
		super(_INVALID_EDUCATION_LEVEL_TYPE_CODE.getCode(), _INVALID_EDUCATION_LEVEL_TYPE_CODE.getMessage());

	}
}
