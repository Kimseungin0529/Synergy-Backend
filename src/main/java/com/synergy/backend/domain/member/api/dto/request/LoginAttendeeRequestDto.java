package com.synergy.backend.domain.member.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "참가자 로그인 요청 DTO")
public record LoginAttendeeRequestDto(
	@Schema(description = "이메일", example = "jiwon.kim@example.com")
	String email,

	@Schema(description = "비밀번호", example = "password1")
	String password
) {}
