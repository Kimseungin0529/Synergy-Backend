package com.synergy.backend.domain.point.exception;

import static com.synergy.backend.domain.point.exception.ErrorType.*;

import com.synergy.backend.global.exception.BaseErrorException;

public class PointNotFoundException extends BaseErrorException {
	public PointNotFoundException() {
		super(_POINT_NOT_FOUND.getCode(), _POINT_NOT_FOUND.getMessage());
	}
}
