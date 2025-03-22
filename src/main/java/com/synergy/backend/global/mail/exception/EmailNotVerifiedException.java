package com.synergy.backend.global.mail.exception;

import static com.synergy.backend.global.mail.exception.ErrorType.*;

import com.synergy.backend.global.exception.BaseErrorException;

public class EmailNotVerifiedException extends BaseErrorException {
	public EmailNotVerifiedException() {
		super(_EMAIL_NOT_VERIFIED.getCode(), _EMAIL_NOT_VERIFIED.getMessage());
	}
}
