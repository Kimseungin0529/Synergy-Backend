package com.synergy.backend.domain.job.exception;

import static com.synergy.backend.domain.job.exception.ErrorType.*;

import com.synergy.backend.global.exception.BaseErrorException;

public class NotFoundJobPositionException extends BaseErrorException {

	public NotFoundJobPositionException() {
		super(_JOB_POSITION_NOT_FOUND.getCode(), _JOB_POSITION_NOT_FOUND.getMessage());
	}
}
