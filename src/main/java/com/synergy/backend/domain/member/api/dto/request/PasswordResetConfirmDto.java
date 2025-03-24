package com.synergy.backend.domain.member.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordResetConfirmDto(
	@NotBlank(message = "이메일은 필수 입력 값입니다.")
	@Email
	String email,

	@NotBlank(message = "비밀번호는 필수 입력 값입니다.")
	@Size(min = 8, max = 20, message = "비밀번호는 8~20자 이내여야 합니다.")
	String newPassword
) {
}
