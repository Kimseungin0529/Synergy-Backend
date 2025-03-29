package com.synergy.backend.domain.member.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailVerificationConfirmDto(
	@NotBlank(message = "이메일은 필수입니다.")
	@Schema(description = "이메일 주소", example = "user@example.com")
	@Email
	String email,

	@NotBlank(message = "인증 코드는 필수입니다.")
	@Schema(description = "이메일로 발송된 인증 코드", example = "493851")
	String code
) {
}
