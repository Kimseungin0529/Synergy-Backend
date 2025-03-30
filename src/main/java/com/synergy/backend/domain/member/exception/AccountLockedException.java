package com.synergy.backend.domain.member.exception;

import static com.synergy.backend.domain.member.exception.ErrorType.*;

import com.synergy.backend.global.exception.BaseErrorException;

public class AccountLockedException extends BaseErrorException {
	public AccountLockedException() {
		super(_ACCOUNT_LOCKED.getCode(), _ACCOUNT_LOCKED.getMessage());
	}
}
