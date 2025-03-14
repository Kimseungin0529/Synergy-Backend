package com.synergy.backend.domain.member.service;

import com.synergy.backend.domain.member.api.dto.request.SignupAttendeeRequestDto;
import com.synergy.backend.domain.member.api.dto.resposne.SignupAttendeeResponseDto;
import com.synergy.backend.domain.member.api.dto.resposne.TokenResponseDto;

import jakarta.validation.Valid;

public interface AuthService {
	SignupAttendeeResponseDto registerAttendee(@Valid SignupAttendeeRequestDto request);

	TokenResponseDto loginAsAttendee(String email, String password);

	TokenResponseDto loginAsAdminOrRecruiter(String authCode);
}
