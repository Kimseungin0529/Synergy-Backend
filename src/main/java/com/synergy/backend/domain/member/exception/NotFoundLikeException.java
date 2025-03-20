package com.synergy.backend.domain.member.exception;

import static com.synergy.backend.domain.member.exception.ErrorType.*;

import com.synergy.backend.global.exception.BaseErrorException;

public class NotFoundLikeException extends BaseErrorException {
	public NotFoundLikeException() {
		super(_LIKE_NOT_FOUND_.getCode(), _LIKE_NOT_FOUND_.getMessage());
	}
}
