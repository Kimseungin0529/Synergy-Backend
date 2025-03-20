package com.synergy.backend.domain.member.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.synergy.backend.domain.member.api.dto.resposne.AttendeeLevelRankingResponseDto;
import com.synergy.backend.domain.member.api.dto.resposne.AttendeePointRankingResponseDto;
import com.synergy.backend.domain.member.entity.details.MembershipLevelType;
import com.synergy.backend.domain.member.service.AdminService;
import com.synergy.backend.global.common.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminService;

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/attendees/level-rankings")
	public ApiResponse<Page<AttendeeLevelRankingResponseDto>> getAttendeeLevelRankings(
		@RequestParam(required = false) MembershipLevelType membershipLevelType,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		return ApiResponse.ok(adminService.getAttendeeLevelRankings(membershipLevelType, PageRequest.of(page, size)), 200);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/attendees/point-rankings")
	public ApiResponse<Page<AttendeePointRankingResponseDto>> getAttendeePointRankings(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		return ApiResponse.ok(adminService.getAttendeePointRankings(PageRequest.of(page, size)), 200);
	}
}
