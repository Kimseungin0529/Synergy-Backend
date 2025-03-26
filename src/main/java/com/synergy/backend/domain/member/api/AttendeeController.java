package com.synergy.backend.domain.member.api;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.synergy.backend.domain.member.api.dto.request.JobInfoDetailsRequestDto;
import com.synergy.backend.domain.member.api.dto.request.JobInfoRequestDto;
import com.synergy.backend.domain.member.api.dto.resposne.AttendeeFullInfoResponseDto;
import com.synergy.backend.domain.member.api.dto.resposne.JobInfoResponseDto;
import com.synergy.backend.domain.member.api.dto.resposne.LikedRecruiterResponseDto;
import com.synergy.backend.domain.member.api.dto.resposne.MyInfoResponseDto;
import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.domain.member.service.AttendeeService;
import com.synergy.backend.domain.member.service.RecruiterAttendeeLikeService;
import com.synergy.backend.global.annotation.SwaggerSummaryRole;
import com.synergy.backend.global.common.ApiResponse;
import com.synergy.backend.global.security.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Attendee Controller", description = "참가자 관련 API")
@RestController
@RequestMapping("/api/v1/attendee")
@RequiredArgsConstructor
public class AttendeeController {

	private final AttendeeService attendeeService;
	private final RecruiterAttendeeLikeService recruiterAttendeeLikeService;

	@Operation(summary = "참가자 현재 직무 정보 등록", description = "온보딩 과정에서 참가자의 관심 분야와 현재 직무 정보를 등록합니다.")
	@SwaggerSummaryRole({RoleType.ATTENDEE})
	@PreAuthorize("hasRole('ATTENDEE')")
	@PatchMapping(path = "/onboarding/job-info")
	public ApiResponse<JobInfoResponseDto> addJobInfo(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@Valid @RequestBody JobInfoRequestDto request) {

		String identifier = userDetails.getIdentifier();
		attendeeService.addJobInfo(identifier, request);
		return ApiResponse.ok(null, 200);
	}

	@Operation(
		summary = "참가자 상세 직무 정보 등록",
		description = "온보딩 과정에서 참가자의 희망 직무, 경력 및 기술 스택 등 상세 직무 정보를 등록합니다."
	)
	@SwaggerSummaryRole({RoleType.ATTENDEE})
	@PreAuthorize("hasRole('ATTENDEE')")
	@PatchMapping(path = "/onboarding/job-info-details", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ApiResponse<JobInfoResponseDto> addJobInfoDetails(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@Valid @RequestPart("request") JobInfoDetailsRequestDto request,
		@RequestPart(value = "multipartFile") MultipartFile multipartFile) {

		String identifier = userDetails.getIdentifier();
		attendeeService.addJobInfoDetails(identifier, request, multipartFile);
		return ApiResponse.ok(null, 200);
	}

	@Operation(summary = "내 정보 조회", description = "로그인된 참가자의 프로필 정보를 조회합니다.")
	@SwaggerSummaryRole({RoleType.ATTENDEE})
	@PreAuthorize("hasRole('ATTENDEE')")
	@GetMapping(path = "/my")
	public ApiResponse<MyInfoResponseDto> getMyInformation(
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {
		String identifier = userDetails.getIdentifier();
		return ApiResponse.ok(attendeeService.getMyInformation(identifier), 200);
	}

	@Operation(summary = "참가자 상세 정보 조회", description = "특정 참가자의 상세 프로필 정보를 조회합니다.")
	@SwaggerSummaryRole({RoleType.ADMIN, RoleType.RECRUITER, RoleType.ATTENDEE})
	@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or (hasRole('ATTENDEE') and #attendeeId == principal.id)")
	@GetMapping(path = "/{attendeeId}")
	public ApiResponse<AttendeeFullInfoResponseDto> getAttendeeInfoDetail(
		@PathVariable Long attendeeId,
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {
		String identifier = userDetails.getIdentifier();
		RoleType role = userDetails.getRole();
		return ApiResponse.ok(attendeeService.getAttendeeInfoDetail(attendeeId, identifier, role), 200);
	}

	@Operation(summary = "좋아요한 채용담당자 목록 조회", description = "참가자를 좋아요한 채용담당자들의 목록을 조회합니다.")
	@SwaggerSummaryRole({RoleType.ATTENDEE})
	@PreAuthorize("hasRole('ATTENDEE')")
	@GetMapping("/liked-recruiters")
	public ApiResponse<List<LikedRecruiterResponseDto>> getLikedRecruiters(
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		return ApiResponse.ok(recruiterAttendeeLikeService.getLikedRecruiters(userDetails.getId()), 200);
	}

}
