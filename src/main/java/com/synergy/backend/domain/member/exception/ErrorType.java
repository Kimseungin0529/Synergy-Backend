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
	_DUPLICATE_EMAIL(409, "이미 등록된 이메일입니다."),
	_INVALID_AGE_GROUP_CODE(400, "유효하지 않은 연령대 코드입니다."),
	_INVALID_CONFERENCE_PARTICIPATION_PURPOSE_CODE(400, "유효하지 않은 컨퍼런스 참여목적 코드입니다."),
	_INVALID_EDUCATION_LEVEL_TYPE_CODE(400, "유효하지 않은 학력 코드입니다."),
	_INVALID_EXPERIENCE_LEVEL_TYPE_CODE(400, "유효하지 않은 경력 코드입니다."),
	_INVALID_PREFERRED_CORPORATE_CULTURE_CODE(400, "유효하지 않은 선호기업문화 코드입니다."),
	_INVALID_REGION_TYPE_CODE(400, "유효하지 않은 지역코드입니다."),
	_INVALID_WORKPLACE_SELECTION_FACTOR_CODE(400, "유효하지 않은 직장선택요소 코드입니다."),
	_DUPLICATE_LIKE(409, "해당 좋아요는 이미 반영되었습니다."),
	_LIKE_NOT_FOUND(400, "해당 좋아요를 찾을 수 없습니다."),
	_INVALID_ACCOUNT_INFORMATION(400, "입력하신 계정정보가 일치하지 않습니다."),
	_SAME_AS_PREVIOUS_PASSWORD(400, "이전에 사용한 비밀번호와 동일한 비밀번호는 사용할 수 없습니다."),
	;

	private final int code;
	private final String message;
}
