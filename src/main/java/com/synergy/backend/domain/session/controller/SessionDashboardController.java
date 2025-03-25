package com.synergy.backend.domain.session.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.synergy.backend.domain.session.dto.sessionparticipateDto.SessionParticipateRateDetailResDto;
import com.synergy.backend.domain.session.dto.sessionparticipateDto.SessionParticipateRateResDto;
import com.synergy.backend.domain.session.service.SessionParticipateService;
import com.synergy.backend.global.common.ApiResponse;
import com.synergy.backend.global.security.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Session Dashboard Controller", description = "세션 대시보드 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dashboard/conference/{conferenceId}")
public class SessionDashboardController {

	private final SessionParticipateService sessionParticipateService;

	@Operation(
		summary = "세션별 참여율 조회",
		description = "컨퍼런스 ID에 해당하는 세션들의 전체 참여율(%)을 조회합니다."
	)
	@GetMapping
	public ApiResponse<List<SessionParticipateRateResDto>> getSessionParticipateRates(
		@AuthenticationPrincipal CustomUserDetails user,
		@PathVariable Long conferenceId) {

		return ApiResponse.ok(sessionParticipateService.getSessionParticipateRate(user.getIdentifier(), conferenceId),
			200);
	}

	@Operation(
		summary = "세션별 상세 참여율 조회",
		description = "컨퍼런스 ID에 해당하는 세션들의 상세 참여율 정보를 조회합니다."
	)
	@GetMapping("/detail")
	public ApiResponse<List<SessionParticipateRateDetailResDto>> getSessionParticipateRateDetails(
		@AuthenticationPrincipal CustomUserDetails user,
		@PathVariable Long conferenceId) {

		return ApiResponse.ok(
			sessionParticipateService.getSessionParticipateRateDetail(user.getIdentifier(), conferenceId), 200);
	}
}
