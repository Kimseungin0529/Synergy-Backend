package com.synergy.backend.domain.member.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "이메일 인증 요청/확인 목적", example = "SIGNUP")
public enum VerificationPurpose {
	SIGNUP,
	PASSWORD_RESET,
	UNLOCK_ACCOUNT
}
