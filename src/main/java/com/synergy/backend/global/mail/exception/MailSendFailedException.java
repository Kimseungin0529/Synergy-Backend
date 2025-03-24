package com.synergy.backend.global.mail.exception;

import static com.synergy.backend.global.mail.exception.ErrorType.*;

import com.synergy.backend.global.exception.BaseErrorException;

public class MailSendFailedException extends BaseErrorException {
	public MailSendFailedException() {
		super(_MAIL_SEND_FAILED.getCode(), _MAIL_SEND_FAILED.getMessage());
	}

	public MailSendFailedException(String message) {
		super(_MAIL_SEND_FAILED.getCode(), message);
	}
}
