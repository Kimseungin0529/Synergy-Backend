package com.synergy.backend.domain.member.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "참가자 로그인 요청 DTO")
public record LoginAttendeeRequestDto(
	@NotBlank(message = "이메일은 필수입니다.")
	@Schema(description = "이메일", example = "jiwon.kim@example.com")
	String email,

	@NotBlank(message = "비밀번호는 필수입니다.")
	@Schema(description = "비밀번호", example = "password1")
	String password
) {}
