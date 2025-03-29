package com.synergy.backend.domain.member.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PasswordResetRequestDto(
	@NotBlank(message = "이름은 필수 입력 값입니다.")
	@Schema(description = "사용자 이름", example = "김이름")
	String name,

	@NotBlank(message = "이메일은 필수 입력 값입니다.")
	@Schema(description = "가입한 이메일 주소", example = "registered@example.com")
	@Email
	String email,

	@NotBlank(message = "휴대폰 번호는 필수 입력 값입니다.")
	@Schema(description = "휴대폰 번호", example = "01012345678")
	@Pattern(regexp = "^\\d{10,11}$", message = "휴대폰 번호는 10~11자리 숫자여야 합니다.")
	String phone
) {
}
