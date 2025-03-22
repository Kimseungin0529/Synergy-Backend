package com.synergy.backend.global.mail;

import static com.synergy.backend.global.mail.ErrorType.*;

import com.synergy.backend.global.exception.BaseErrorException;

public class VerificationCodeExpiredException extends BaseErrorException {
	public VerificationCodeExpiredException() {
		super(_VERIFICATION_CODE_EXPIRED.getCode(), _VERIFICATION_CODE_EXPIRED.getMessage());

	}
}
