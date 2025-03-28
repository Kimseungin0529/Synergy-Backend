package com.synergy.backend.domain.member.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "관리자 로그인 요청 DTO")
public record LoginAdminRequestDto(
	@NotBlank(message = "관리자 인증 코드는 필수입니다.")
	@Schema(description = "관리자 인증 코드", example = "ADM12345")
	String adminAuthCode
) {
}
