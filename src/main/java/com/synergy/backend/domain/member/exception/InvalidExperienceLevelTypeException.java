package com.synergy.backend.domain.member.exception;

import static com.synergy.backend.domain.member.exception.ErrorType.*;

import com.synergy.backend.global.exception.BaseErrorException;

public class InvalidExperienceLevelTypeException extends BaseErrorException {
	public InvalidExperienceLevelTypeException() {
		super(_INVALID_EXPERIENCE_LEVEL_TYPE_CODE.getCode(), _INVALID_EXPERIENCE_LEVEL_TYPE_CODE.getMessage());

	}
}
