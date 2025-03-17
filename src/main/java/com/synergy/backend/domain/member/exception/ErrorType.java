package com.synergy.backend.domain.member.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorType {

	_USER_NOT_FOUND(400, "해당 사용자를 찾을 수 없습니다."),
	_RECRUITER_NOT_FOUND(400, "해당 채용담당자를 찾을 수 없습니다."),
	_ACCESS_DENIED(403, "접근 권한이 없습니다."),
	_ADMIN_OR_RECRUITER_NOT_FOUND(404, "해당 관리자나 채용담당자를 찾을 수 없습니다."),
	_INVALID_AUTH_CODE(400, "유효하지 않은 인증코드 입니다."),
	_INVALID_EMAIL_OR_PASSWORD(400, "이메일이 존재하지 않거나 비밀번호가 다릅니다."),
	_DUPLICATE_EMAIL_EXCEPTION(409, "이미 등록된 이메일입니다.")

	;

	private final int code;
	private final String message;
}
