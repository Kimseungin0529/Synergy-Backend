package com.synergy.backend.domain.booth.controller;

import java.util.List;

import com.synergy.backend.domain.booth.dto.boothParticipateDto.BoothParticipationInterestedResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.synergy.backend.domain.booth.service.BoothParticipationService;
import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.global.annotation.SwaggerSummaryRole;
import com.synergy.backend.global.common.ApiResponse;

import lombok.RequiredArgsConstructor;

@Tag(name = "Booth Dashboard Controller", description = "부스 대시보드 관련 API")
@RestController
@RequestMapping("/api/v1/dashboard/conference/{conferenceId}")
@RequiredArgsConstructor
public class BoothDashboardController {

	private final BoothParticipationService boothParticipationService;

	@SwaggerSummaryRole({RoleType.ADMIN})
	@GetMapping("/booths/participation/interest")
	public ApiResponse<List<BoothParticipationInterestedResponseDto>> getParticipationCountByInterest(
		@PathVariable Long conferenceId) {
		List<BoothParticipationInterestedResponseDto> result = boothParticipationService.getParticipationCountByInterest(conferenceId);
		return ApiResponse.ok(result, 200);
	}
}
