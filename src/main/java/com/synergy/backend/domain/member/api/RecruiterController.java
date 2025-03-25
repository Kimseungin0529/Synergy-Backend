package com.synergy.backend.domain.member.api;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.synergy.backend.domain.member.api.dto.AttendeeFilterRequest;
import com.synergy.backend.domain.member.api.dto.AttendeeListResponse;
import com.synergy.backend.domain.member.api.dto.resposne.AttendeeSimpleResponseDto;
import com.synergy.backend.domain.member.api.dto.resposne.RecruiterMyInfoResponseDto;
import com.synergy.backend.domain.member.service.AttendeeDetailResponse;
import com.synergy.backend.domain.member.service.RecruiterAttendeeLikeService;
import com.synergy.backend.domain.member.service.RecruiterService;
import com.synergy.backend.global.common.ApiResponse;
import com.synergy.backend.global.security.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Recruiter Controller", description = "채용담당자 관련 API")
@RestController
@RequestMapping("/api/v1/recruiter")
@RequiredArgsConstructor
public class RecruiterController {

	private final RecruiterService recruiterService;
	private final RecruiterAttendeeLikeService recruiterAttendeeLikeService;

	@Operation(summary = "내 정보 조회", description = "로그인된 채용담당자의 정보를 조회합니다.")
	@PreAuthorize("hasRole('RECRUITER')")
	@GetMapping("/my")
	public ApiResponse<RecruiterMyInfoResponseDto> getMyInformation(
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		return ApiResponse.ok(recruiterService.getMyInformation(userDetails.getId()), 200);
	}

	@Operation(summary = "참가자 좋아요", description = "채용담당자가 특정 참가자에게 좋아요를 누릅니다.")
	@PreAuthorize("hasRole('RECRUITER')")
	@PostMapping("/attendees/{attendeeId}/like")
	public ApiResponse<?> likeAttendee(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable(value = "attendeeId") Long attendeeId
	) {
		recruiterAttendeeLikeService.likeAttendee(userDetails.getId(), attendeeId);
		return ApiResponse.ok(null, 200);
	}

	@Operation(summary = "참가자 좋아요 취소", description = "채용담당자가 특정 참가자에게 눌렀던 좋아요를 취소합니다.")
	@PreAuthorize("hasRole('RECRUITER')")
	@DeleteMapping("/attendees/{attendeeId}/unlike")
	public ApiResponse<?> unlikeAttendee(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable(value = "attendeeId") Long attendeeId
	) {
		recruiterAttendeeLikeService.unlikeAttendee(userDetails.getId(), attendeeId);
		return ApiResponse.ok(null, 200);
	}

	@Operation(summary = "좋아요한 참가자 목록 조회", description = "채용담당자가 좋아요한 참가자 목록을 조회합니다.")
	@PreAuthorize("hasRole('RECRUITER')")
	@GetMapping("/me/liked-attendees")
	public ApiResponse<List<AttendeeSimpleResponseDto>> getLikedAttendees(
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		return ApiResponse.ok(recruiterAttendeeLikeService.getLikedAttendees(userDetails.getId()), 200);
	}

	@Operation(
		summary = "채용희망 참가자 필터링 조회",
		description = """
			채용담당자가 채용 희망에 동의한 참가자들의 목록을 조건에 맞춰 필터링합니다.
			필터 조건은 직무, 학력, 연령대, 경력, 희망 근무 지역을 선택할 수 있습니다.
			페이징 처리를 통해 응답을 분할하여 제공합니다.
			"""
	)
	@PreAuthorize("hasRole('RECRUITER') or hasRole('ADMIN')")
	@GetMapping("/{id}/attendees")
	public ApiResponse<AttendeeListResponse> getAttendees(
		@Parameter(description = "페이징 처리 정보 (기본: page=0, size=20)") @PageableDefault(page = 0, size = 20) Pageable pageable,
		@Parameter(description = "채용담당자 ID") @PathVariable("id") Long recruiterId,
		@Parameter(description = "희망 직무 리스트") @RequestParam(required = false) List<String> occupations,
		@Parameter(description = "학력") @RequestParam(required = false) String educationLevel,
		@Parameter(description = "연령대") @RequestParam(required = false) String ageGroup,
		@Parameter(description = "경력") @RequestParam(required = false) String experienceLevel,
		@Parameter(description = "희망 근무 지역 리스트") @RequestParam(required = false) List<String> regions
	) {

		AttendeeFilterRequest requestCondition = AttendeeFilterRequest.of(occupations, educationLevel, ageGroup,
			experienceLevel, regions);
		return ApiResponse.ok(recruiterService.getAttendeesBy(pageable, recruiterId, requestCondition), 200);
	}
}
