package com.synergy.backend.domain.booth.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.synergy.backend.domain.booth.dto.BoothParticipationResponseDto;
import com.synergy.backend.domain.booth.service.BoothParticipationService;
import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.global.annotation.SwaggerSummaryRole;
import com.synergy.backend.global.common.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/dashboard/conference/{conferenceId}")
@RequiredArgsConstructor
public class BoothDashboardController {

	private final BoothParticipationService boothParticipationService;

	@SwaggerSummaryRole({RoleType.ADMIN})
	@GetMapping("/booths/{boothId}/participation/interest")
	public ApiResponse<List<BoothParticipationResponseDto>> getParticipationCountByInterest(
		@PathVariable Long conferenceId,
		@PathVariable Long boothId) {
		List<BoothParticipationResponseDto> result = boothParticipationService.getParticipationCountByInterest(boothId);
		return ApiResponse.ok(result, 200);
	}
}
