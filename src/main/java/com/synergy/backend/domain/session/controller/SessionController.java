package com.synergy.backend.domain.session.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.zxing.WriterException;
import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.domain.session.dto.sessionDto.SessionDetailResDto;
import com.synergy.backend.domain.session.dto.sessionDto.SessionReqDto;
import com.synergy.backend.domain.session.dto.sessionDto.SessionResDto;
import com.synergy.backend.domain.session.service.SessionService;
import com.synergy.backend.global.annotation.SwaggerSummaryRole;
import com.synergy.backend.global.common.ApiResponse;
import com.synergy.backend.global.security.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Session Controller", description = "세션 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/conference/{conferenceId}/session")
public class SessionController {

	private final SessionService sessionService;

	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "세션 생성", description = "관리자가 세션을 생성합니다.")
	@SwaggerSummaryRole({RoleType.ADMIN})
	@PostMapping
	public ApiResponse createSession(@AuthenticationPrincipal CustomUserDetails user,
		HttpServletRequest request,
		@PathVariable(name = "conferenceId") Long conferenceId,
		@RequestPart @Valid SessionReqDto sessionReqDto,
		@RequestPart MultipartFile multipartFile) throws WriterException {

		String router = request.getHeader("Origin");
		sessionService.createSession(user.getIdentifier(), router, conferenceId, sessionReqDto, multipartFile);

		return ApiResponse.ok("Session created successfully!", 200);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'ATTENDEE', 'RECRUITER')")
	@Operation(summary = "세션 목록 조회", description = "관리자, 참가자, 채용담당자가 세션 목록을 조회합니다.")
	@SwaggerSummaryRole({RoleType.ADMIN, RoleType.RECRUITER, RoleType.ATTENDEE})
	@GetMapping
	public ApiResponse getSessions(@AuthenticationPrincipal CustomUserDetails user,
		@PathVariable(name = "conferenceId") Long conferenceId) {
		List<SessionResDto> result = sessionService.getSessions(user.getIdentifier(), conferenceId);

		return ApiResponse.ok(result, 200);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'ATTENDEE', 'RECRUITER')")
	@Operation(summary = "세션 상세 조회", description = "관리자, 참가자, 채용담당자가 특정 세션의 상세 정보를 조회합니다.")
	@SwaggerSummaryRole({RoleType.ADMIN, RoleType.RECRUITER, RoleType.ATTENDEE})
	@GetMapping("/{sessionId}")
	public ApiResponse getSession(@AuthenticationPrincipal CustomUserDetails user,
		@PathVariable(name = "conferenceId") Long conferenceId,
		@PathVariable(name = "sessionId") Long sessionId) {
		SessionDetailResDto result = sessionService.getSessionInfo(user.getIdentifier(), user.getRole(), conferenceId,
			sessionId);

		return ApiResponse.ok(result, 200);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "세션 수정", description = "관리자가 세션 정보를 수정합니다.")
	@SwaggerSummaryRole({RoleType.ADMIN})
	@PatchMapping("/{sessionId}")
	public ApiResponse updateSession(@AuthenticationPrincipal CustomUserDetails user,
		@PathVariable Long sessionId,
		@RequestPart @Valid SessionReqDto sessionReqDto,
		@RequestPart MultipartFile multipartFile) {
		sessionService.updateSession(user.getIdentifier(), sessionId, sessionReqDto, multipartFile);

		return ApiResponse.ok("Session updated successfully!", 200);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "세션 삭제", description = "관리자가 세션을 삭제합니다.")
	@SwaggerSummaryRole({RoleType.ADMIN})
	@DeleteMapping("/{sessionId}")
	public ApiResponse deleteSession(@AuthenticationPrincipal CustomUserDetails user,
		@PathVariable Long sessionId) {
		sessionService.deleteSession(user.getIdentifier(), sessionId);

		return ApiResponse.ok("Session deleted successfully!", 200);
	}

}
