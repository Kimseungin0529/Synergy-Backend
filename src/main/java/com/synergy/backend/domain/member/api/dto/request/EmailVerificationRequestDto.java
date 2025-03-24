package com.synergy.backend.domain.member.api.dto.request;

import jakarta.validation.constraints.Email;

public record EmailVerificationRequestDto(
	@Email
	String email

) {

}
