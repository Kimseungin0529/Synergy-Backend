package com.synergy.backend.domain.job.exception;

import static com.synergy.backend.domain.job.exception.ErrorType.*;

import com.synergy.backend.global.exception.BaseErrorException;

public class NotFoundOccupationCategoryException extends BaseErrorException {

	public NotFoundOccupationCategoryException() {
		super(_OCCUPATION_CATEGORY_NOT_FOUND.getCode(), _OCCUPATION_CATEGORY_NOT_FOUND.getMessage());
	}
}
