package com.synergy.backend.domain.member.exception;

import static com.synergy.backend.domain.member.exception.ErrorType.*;

import com.synergy.backend.global.exception.BaseErrorException;

public class InvalidRegionTypeCodeException extends BaseErrorException {
	public InvalidRegionTypeCodeException() {
		super(_INVALID_REGION_TYPE_CODE.getCode(), _INVALID_REGION_TYPE_CODE.getMessage());

	}
}
