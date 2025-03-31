package com.synergy.backend.domain.member.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.synergy.backend.domain.member.api.dto.response.AttendeeLevelRankingResponseDto;
import com.synergy.backend.domain.member.api.dto.response.AttendeePointRankingResponseDto;
import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.domain.member.entity.details.MembershipLevelType;
import com.synergy.backend.domain.member.service.AdminService;
import com.synergy.backend.global.annotation.SwaggerSummaryRole;
import com.synergy.backend.global.common.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Admin Controller", description = "관리자의 회원 랭킹 조회 API")
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminService;

	@Operation(summary = "등급별 회원 랭킹 조회", description = "회원 등급(MembershipLevelType)에 따라 회원의 랭킹을 조회합니다.")
	@SwaggerSummaryRole({RoleType.ADMIN})
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/conferences/{conferenceId}/attendees/level-rankings")
	public ApiResponse<Page<AttendeeLevelRankingResponseDto>> getAttendeeLevelRankings(
		@Parameter(description = "조회할 컨퍼런스 ID", example = "1") @PathVariable Long conferenceId,
		@RequestParam(required = false) MembershipLevelType membershipLevelType,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		return ApiResponse.ok(
			adminService.getAttendeeLevelRankings(conferenceId, membershipLevelType, PageRequest.of(page, size)),
			200);
	}

	@Operation(summary = "포인트 랭킹 조회", description = "회원의 누적 포인트를 기준으로 랭킹을 조회합니다.")
	@SwaggerSummaryRole({RoleType.ADMIN})
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/conferences/{conferenceId}/attendees/point-rankings")
	public ApiResponse<Page<AttendeePointRankingResponseDto>> getAttendeePointRankings(
		@Parameter(description = "조회할 컨퍼런스 ID", example = "1") @PathVariable Long conferenceId,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		return ApiResponse.ok(adminService.getAttendeePointRankings(conferenceId, PageRequest.of(page, size)), 200);
	}
}
