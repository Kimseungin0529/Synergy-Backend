package com.synergy.backend.domain.conference.exception;

import static com.synergy.backend.domain.conference.exception.ErrorType.*;

import com.synergy.backend.global.exception.BaseErrorException;

public class InvalidTicketCodeException extends BaseErrorException {

	public InvalidTicketCodeException() {
		super(_INVALID_TICKET_CODE.getCode(), _INVALID_TICKET_CODE.getMessage());
	}
}
