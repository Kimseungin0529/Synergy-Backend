package com.synergy.backend.domain.job.exception;

import static com.synergy.backend.domain.job.exception.ErrorType.*;

import com.synergy.backend.global.exception.BaseErrorException;

public class NotFoundJobCategoryException extends BaseErrorException {

	public NotFoundJobCategoryException() {
		super(_JOB_CATEGORY_NOT_FOUND.getCode(), _JOB_CATEGORY_NOT_FOUND.getMessage());
	}
}
