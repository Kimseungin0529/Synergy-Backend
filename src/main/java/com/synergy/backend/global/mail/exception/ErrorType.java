package com.synergy.backend.global.mail.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorType {

	_EMAIL_NOT_VERIFIED(403, "이메일 인증이 완료되지 않았습니다."),
	_VERIFICATION_CODE_EXPIRED(400, "인증번호가 존재하지 않거나 만료되었습니다."),
	_VERIFICATION_CODE_MISMATCH(400, "인증번호가 일치하지 않습니다."),
	_MAIL_SEND_FAILED(500, "메일 전송에 실패했습니다.");

	private final int code;
	private final String message;
}
