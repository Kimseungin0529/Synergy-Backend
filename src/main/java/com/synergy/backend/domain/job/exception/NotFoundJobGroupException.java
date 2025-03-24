package com.synergy.backend.domain.job.exception;

import static com.synergy.backend.domain.job.exception.ErrorType.*;

import com.synergy.backend.global.exception.BaseErrorException;

public class NotFoundJobGroupException extends BaseErrorException {

	public NotFoundJobGroupException() {
		super(_JOB_GROUP_NOT_FOUND.getCode(), _JOB_GROUP_NOT_FOUND.getMessage());
	}
}
