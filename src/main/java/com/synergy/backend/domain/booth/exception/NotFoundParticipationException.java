package com.synergy.backend.domain.booth.exception;

import com.synergy.backend.global.exception.BaseErrorException;

import static com.synergy.backend.domain.booth.exception.ErrorType._NOT_FOUND_PARTICIPATION;

public class NotFoundParticipationException extends BaseErrorException {
  public NotFoundParticipationException() {
    super(_NOT_FOUND_PARTICIPATION.getCode(), _NOT_FOUND_PARTICIPATION.getMessage());
  }

  public NotFoundParticipationException(String message) {
    super(_NOT_FOUND_PARTICIPATION.getCode(), message);
  }
}
