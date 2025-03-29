package com.synergy.backend.global.util.file.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorType {

	_FILE_UPLOAD_FAIL(400, "잘못된 파일 형식이 존재합니다."),
	_EXTERNAL_API_FAIL_CALL(500, "외부 API 호출 실패했습니다."),
	_EMPTY_IMAGE_FILE(400, "이미지 파일이 비어 있습니다."),
	;

	private final int code;
	private final String message;
}
