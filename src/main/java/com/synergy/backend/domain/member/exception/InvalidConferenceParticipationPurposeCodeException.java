package com.synergy.backend.domain.member.exception;

import static com.synergy.backend.domain.member.exception.ErrorType.*;

import com.synergy.backend.global.exception.BaseErrorException;

public class InvalidConferenceParticipationPurposeCodeException extends BaseErrorException {
	public InvalidConferenceParticipationPurposeCodeException() {
		super(_INVALID_CONFERENCE_PARTICIPATION_PURPOSE_CODE.getCode(),
			_INVALID_CONFERENCE_PARTICIPATION_PURPOSE_CODE.getMessage());

	}
}
