package com.synergy.backend.domain.member.exception;

import static com.synergy.backend.domain.member.exception.ErrorType.*;

import com.synergy.backend.global.exception.BaseErrorException;

public class InvalidPreferredCorporateCultureCodeException extends BaseErrorException {
	public InvalidPreferredCorporateCultureCodeException() {
		super(_INVALID_PREFERRED_CORPORATE_CULTURE_CODE.getCode(), _INVALID_PREFERRED_CORPORATE_CULTURE_CODE.getMessage());

	}
}
