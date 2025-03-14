package com.synergy.backend.domain.member.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignupAttendeeRequestDto(
	@NotBlank(message = "이름은 필수 입력 값입니다.")
	@Size(min = 2, max = 30, message = "이름은 2~30자 이내여야 합니다.")
	String name,

	@NotBlank(message = "이메일은 필수 입력 값입니다.")
	@Email(message = "올바른 이메일 형식이어야 합니다.")
	String email,

	@NotBlank(message = "비밀번호는 필수 입력 값입니다.")
	@Size(min = 8, max = 20, message = "비밀번호는 8~20자 이내여야 합니다.")
	String password,

	@NotBlank(message = "전화번호는 필수 입력 값입니다.")
	@Pattern(regexp = "^\\d{10,11}$", message = "전화번호는 10~11자리 숫자여야 합니다.")
	String phone
	// 인증 코드 추가
) {

}
