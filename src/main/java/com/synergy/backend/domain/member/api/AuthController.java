package com.synergy.backend.domain.member.api;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.synergy.backend.domain.member.api.dto.request.EmailVerificationConfirmDto;
import com.synergy.backend.domain.member.api.dto.request.EmailVerificationRequestDto;
import com.synergy.backend.domain.member.api.dto.request.LoginAdminRequestDto;
import com.synergy.backend.domain.member.api.dto.request.LoginAttendeeRequestDto;
import com.synergy.backend.domain.member.api.dto.request.PasswordResetConfirmDto;
import com.synergy.backend.domain.member.api.dto.request.PasswordResetRequestDto;
import com.synergy.backend.domain.member.api.dto.request.SignupAttendeeRequestDto;
import com.synergy.backend.domain.member.api.dto.response.EmailVerificationResponseForTestDto;
import com.synergy.backend.domain.member.api.dto.response.TokenResponseDto;
import com.synergy.backend.domain.member.service.AuthService;
import com.synergy.backend.domain.member.vo.TokenWithRefreshToken;
import com.synergy.backend.global.annotation.DisableSwaggerSecurity;
import com.synergy.backend.global.common.ApiResponse;
import com.synergy.backend.global.mail.MailService;
import com.synergy.backend.global.token.CookieUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Auth Controller", description = "인증/인가 관련 API")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	private final MailService mailService;
	private final CookieUtils cookieUtils;

	@Operation(summary = "참가자 회원가입", description = "이메일 인증을 완료한 참가자가 회원가입을 합니다.")
	@DisableSwaggerSecurity
	@PostMapping("/attendee/signup")
	public ApiResponse<TokenResponseDto> registerAttendee(@Valid @RequestBody SignupAttendeeRequestDto request,
		HttpServletResponse response) {
		TokenWithRefreshToken tokenWithRefreshToken = authService.registerAttendee(request);

		cookieUtils.addRefreshTokenToCookie(response, tokenWithRefreshToken.refreshToken());

		return ApiResponse.ok(tokenWithRefreshToken.tokenResponseDto(), 201);
	}

	@Operation(summary = "참가자 로그인", description = "참가자가 이메일과 비밀번호로 로그인하여 액세스/리프레시 토큰을 발급받습니다.")
	@DisableSwaggerSecurity
	@PostMapping("/attendee/login")
	public ApiResponse<TokenResponseDto> loginAttendee(@RequestBody LoginAttendeeRequestDto request,
		HttpServletResponse response) {

		TokenWithRefreshToken tokenWithRefreshToken = authService.loginAsAttendee(request.email(), request.password());

		cookieUtils.addRefreshTokenToCookie(response, tokenWithRefreshToken.refreshToken());

		return ApiResponse.ok(tokenWithRefreshToken.tokenResponseDto(), 200);
	}

	@Operation(
		summary = "관리자/채용담당자 로그인",
		description = "관리자 인증 코드로 로그인합니다.",
		requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = LoginAdminRequestDto.class),
				examples = {
					@ExampleObject(
						name = "관리자",
						summary = "관리자 코드 입력 예시",
						value = """
							{
							  "adminAuthCode": "ADM12345"
							}
							"""
					),
					@ExampleObject(
						name = "채용담당자",
						summary = "채용담당자 코드 입력 예시",
						value = """
							{
							  "adminAuthCode": "RC12345"
							}
							"""
					)
				}
			)
		)
	)
	@DisableSwaggerSecurity
	@PostMapping("/admin/login")
	public ApiResponse<TokenResponseDto> loginAdmin(@RequestBody LoginAdminRequestDto request,
		HttpServletResponse response) {

		TokenWithRefreshToken tokenWithRefreshToken = authService.loginAsAdminOrRecruiter(request.adminAuthCode());

		cookieUtils.addRefreshTokenToCookie(response, tokenWithRefreshToken.refreshToken());

		return ApiResponse.ok(tokenWithRefreshToken.tokenResponseDto(), 200);
	}

	@Operation(summary = "비밀번호 재설정 요청", description = "본인 인증을 위해 이메일, 이름, 전화번호를 입력해 비밀번호 재설정을 요청합니다.")
	@DisableSwaggerSecurity
	@PostMapping("/password/reset/request")
	public ApiResponse<?> passwordResetRequest(@Valid @RequestBody PasswordResetRequestDto request) {
		authService.passwordResetRequest(request.email(), request.name(), request.phone());
		return ApiResponse.emptyOk();
	}

	@Operation(summary = "비밀번호 재설정", description = "본인 확인이 완료된 사용자가 새 비밀번호로 비밀번호를 재설정합니다.")
	@DisableSwaggerSecurity
	@PostMapping("/password/reset")
	public ApiResponse<?> newPassword(@Valid @RequestBody PasswordResetConfirmDto request) {
		authService.passwordReset(request.email(), request.newPassword());
		return ApiResponse.emptyOk();
	}

	@Operation(summary = "이메일 인증 요청", description = "입력한 이메일 주소로 인증번호를 전송합니다.")
	@DisableSwaggerSecurity
	@PostMapping("/email/verification/request")
	public ApiResponse<EmailVerificationResponseForTestDto> emailVerificationRequest(
		@Valid @RequestBody EmailVerificationRequestDto request) throws
		MessagingException {
		String code = mailService.sendVerificationCodeToMail(request.email());
		return ApiResponse.ok(new EmailVerificationResponseForTestDto(code), 200);
	}

	@Operation(summary = "이메일 인증 확인", description = "인증번호를 입력하여 이메일 인증을 완료합니다.")
	@DisableSwaggerSecurity
	@PostMapping("/email/verification/confirm")
	public ApiResponse<?> emailVerificationConfirm(@Valid @RequestBody EmailVerificationConfirmDto request) {
		mailService.mailVerificationConfirm(request.email(), request.code());
		switch (request.purpose()) {
			case UNLOCK_ACCOUNT -> authService.unlockAccountIfLocked(request.email());
			case SIGNUP, PASSWORD_RESET -> {
			}
		}

		return ApiResponse.emptyOk();
	}

	@Operation(summary = "리프레시 토큰 재발급", description = "토큰을 통해 리프레시토큰을 전송하면 재발급된 리프레시 토큰을 반환합니다.")
	@DisableSwaggerSecurity
	@PostMapping("/refresh-token/reissue")
	public ApiResponse<TokenResponseDto> reissueRefreshToken(
		@CookieValue(CookieUtils.REFRESH_TOKEN_NAME) String refreshToken,
		HttpServletResponse response) {

		TokenWithRefreshToken tokenWithRefreshToken = authService.reissueRefreshToken(refreshToken);

		cookieUtils.addRefreshTokenToCookie(response, tokenWithRefreshToken.refreshToken());

		return ApiResponse.ok(tokenWithRefreshToken.tokenResponseDto(), 200);
	}

	@Operation(summary = "로그아웃", description = "리프레시 토큰을 무효화하고 로그아웃합니다.")
	@DisableSwaggerSecurity
	@PostMapping("/logout")
	public ApiResponse<?> logout(@CookieValue(CookieUtils.REFRESH_TOKEN_NAME) String refreshToken,
		HttpServletResponse response) {
		authService.logout(refreshToken);

		cookieUtils.deleteRefreshTokenCookie(response);

		return ApiResponse.emptyOk();
	}
}
