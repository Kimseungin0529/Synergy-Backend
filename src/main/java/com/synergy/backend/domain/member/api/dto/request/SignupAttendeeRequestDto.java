package com.synergy.backend.domain.member.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignupAttendeeRequestDto(
	@NotBlank(message = "이름은 필수 입력 값입니다.")
	@Size(min = 2, max = 30, message = "이름은 2~30자 이내여야 합니다.")
	@Schema(description = "사용자 이름", example = "홍길동")
	String name,

	@NotBlank(message = "이메일은 필수 입력 값입니다.")
	@Email(message = "올바른 이메일 형식이어야 합니다.")
	@Schema(description = "사용자 이메일", example = "test@example.com")
	String email,

	@NotBlank(message = "티켓코드는 필수 입력 값입니다.")
	@Schema(description = "사전에 발급받은 티켓코드 (컨퍼런스 마다 생성되는 값임.)")
	String ticketCode,

	@NotBlank(message = "비밀번호는 필수 입력 값입니다.")
	@Size(min = 8, max = 20, message = "비밀번호는 8~20자 이내여야 합니다.")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$",
		message = "비밀번호는 영문자와 숫자를 포함해야 합니다.")
	@Schema(description = "비밀번호 (영문자 + 숫자 포함 8~20 글자)", example = "abc12345")
	String password,

	@NotBlank(message = "휴대폰 번호는 필수 입력 값입니다.")
	@Pattern(regexp = "^\\d{10,11}$", message = "휴대폰 번호는 10~11자리 숫자여야 합니다.")
	@Schema(description = "전화번호 (숫자만)", example = "01012345678")
	String phone
) {

}
