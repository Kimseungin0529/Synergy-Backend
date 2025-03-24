package com.synergy.backend.domain.member.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PasswordResetRequestDto(
	@NotBlank(message = "이름은 필수 입력 값입니다.")
	String name,

	@NotBlank(message = "이메일은 필수 입력 값입니다.")
	@Email
	String email,

	@NotBlank(message = "휴대폰 번호는 필수 입력 값입니다.")
	@Pattern(regexp = "^\\d{10,11}$", message = "휴대폰 번호는 10~11자리 숫자여야 합니다.")
	String phone
) {
}
