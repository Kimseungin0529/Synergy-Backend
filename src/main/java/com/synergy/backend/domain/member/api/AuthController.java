package com.synergy.backend.domain.member.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.synergy.backend.domain.member.api.dto.request.LoginAdminRequestDto;
import com.synergy.backend.domain.member.api.dto.request.LoginAttendeeRequestDto;
import com.synergy.backend.domain.member.api.dto.request.SignupAttendeeRequestDto;
import com.synergy.backend.domain.member.api.dto.resposne.TokenResponseDto;
import com.synergy.backend.domain.member.service.AuthService;
import com.synergy.backend.global.common.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/attendee/signup")
	public ApiResponse<?> registerAttendee(@Valid @RequestBody SignupAttendeeRequestDto request) {
		return ApiResponse.ok(authService.registerAttendee(request), 201);
	}

	@PostMapping("/attendee/login")
	public ApiResponse<TokenResponseDto> loginAttendee(@RequestBody LoginAttendeeRequestDto request) {
		return ApiResponse.ok(authService.loginAsAttendee(request.email(), request.password()), 200);
	}

	@PostMapping("/admin/login")
	public ApiResponse<TokenResponseDto> loginAdmin(@RequestBody LoginAdminRequestDto request) {
		return ApiResponse.ok(authService.loginAsAdminOrRecruiter(request.adminAuthCode()), 200);
	}

	@PostMapping("/email-verification/request")

}
