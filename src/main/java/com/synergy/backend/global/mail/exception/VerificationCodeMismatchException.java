package com.synergy.backend.global.mail.exception;

import static com.synergy.backend.global.mail.exception.ErrorType.*;

import com.synergy.backend.global.exception.BaseErrorException;

public class VerificationCodeMismatchException extends BaseErrorException {
	public VerificationCodeMismatchException() {
		super(_VERIFICATION_CODE_MISMATCH.getCode(), _VERIFICATION_CODE_MISMATCH.getMessage());

	}
}
