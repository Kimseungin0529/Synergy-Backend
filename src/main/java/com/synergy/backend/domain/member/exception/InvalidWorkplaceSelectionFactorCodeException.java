package com.synergy.backend.domain.member.exception;

import static com.synergy.backend.domain.member.exception.ErrorType.*;

import com.synergy.backend.global.exception.BaseErrorException;

public class InvalidWorkplaceSelectionFactorCodeException extends BaseErrorException {
	public InvalidWorkplaceSelectionFactorCodeException() {
		super(_INVALID_WORKPLACE_SELECTION_FACTOR_CODE.getCode(),
			_INVALID_WORKPLACE_SELECTION_FACTOR_CODE.getMessage());

	}
}
