package com.synergy.backend.domain.member.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailVerificationRequestDto(
	@NotBlank(message = "이메일은 필수입니다.")
	@Schema(description = "이메일 주소", example = "user@example.com")
	@Email
	String email

) {

}
