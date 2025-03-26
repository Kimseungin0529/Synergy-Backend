package com.synergy.backend.domain.point.api;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.domain.point.api.dto.PointResponseDto;
import com.synergy.backend.domain.point.service.PointService;
import com.synergy.backend.global.annotation.SwaggerSummaryRole;
import com.synergy.backend.global.common.ApiResponse;
import com.synergy.backend.global.security.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Point Controller", description = "포인트 관련 API")
@RestController
@RequestMapping("/api/v1/points")
@RequiredArgsConstructor
public class PointController {

	private final PointService pointService;

	@Operation(summary = "내 포인트 내역 조회", description = "로그인한 참가자의 포인트 적립 및 사용 내역을 조회합니다.")
	@SwaggerSummaryRole({RoleType.ATTENDEE})
	@PreAuthorize("hasRole('ATTENDEE')")
	@GetMapping("/my-points")
	public ApiResponse<?> getMyPoints(@AuthenticationPrincipal CustomUserDetails userDetails) {
		Long attendeeId = userDetails.getId();
		List<PointResponseDto> response = pointService.getPointResponses(attendeeId);
		return ApiResponse.ok(response, 200);
	}
}
