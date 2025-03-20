package com.synergy.backend.domain.member.exception;

import static com.synergy.backend.domain.member.exception.ErrorType.*;

import com.synergy.backend.global.exception.BaseErrorException;

public class DuplicateLikeException extends BaseErrorException {
	public DuplicateLikeException() {
		super(_DUPLICATE_LIKE.getCode(), _DUPLICATE_LIKE.getMessage());
	}
}
