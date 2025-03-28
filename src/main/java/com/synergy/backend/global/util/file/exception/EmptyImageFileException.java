package com.synergy.backend.global.util.file.exception;

import static com.synergy.backend.global.util.file.exception.ErrorType.*;

import com.synergy.backend.global.exception.BaseErrorException;

public class EmptyImageFileException extends BaseErrorException {

	public EmptyImageFileException() {
		super(_EMPTY_IMAGE_FILE.getCode(), _EMPTY_IMAGE_FILE.getMessage());

	}
}
