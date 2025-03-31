package com.synergy.backend.domain.member.service;

import com.synergy.backend.domain.member.api.dto.request.SignupAttendeeRequestDto;
import com.synergy.backend.domain.member.vo.TokenWithRefreshToken;

public interface AuthService {
	TokenWithRefreshToken registerAttendee(SignupAttendeeRequestDto request);

	TokenWithRefreshToken loginAsAttendee(String email, String password);

	TokenWithRefreshToken loginAsAdminOrRecruiter(String authCode);

	void passwordResetRequest(String email, String name, String phone);

	void passwordReset(String email, String newPassword);

	TokenWithRefreshToken reissueRefreshToken(String refreshToken);

	void logout(String refreshToken);

	void unlockAccountIfLocked(String email);
}
