package com.synergy.backend.domain.member.service;

import com.synergy.backend.domain.member.api.dto.request.SignupAttendeeRequestDto;
import com.synergy.backend.domain.member.api.dto.resposne.SignupAttendeeResponseDto;
import com.synergy.backend.domain.member.vo.TokenWithRefreshToken;

import jakarta.validation.Valid;

public interface AuthService {
	SignupAttendeeResponseDto registerAttendee(@Valid SignupAttendeeRequestDto request);

	TokenWithRefreshToken loginAsAttendee(String email, String password);

	TokenWithRefreshToken loginAsAdminOrRecruiter(String authCode);

	void passwordResetRequest(String email, String name, String phone);

	void passwordReset(String email, String newPassword);

	TokenWithRefreshToken reissueRefreshToken(String refreshToken);

	void logout(String refreshToken);
}
