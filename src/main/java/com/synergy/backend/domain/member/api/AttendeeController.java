package com.synergy.backend.domain.member.api;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.synergy.backend.domain.member.api.dto.request.JobInfoDetailsRequestDto;
import com.synergy.backend.domain.member.api.dto.request.JobInfoRequestDto;
import com.synergy.backend.domain.member.api.dto.resposne.AttendeeFullInfoResponseDto;
import com.synergy.backend.domain.member.api.dto.resposne.JobInfoResponseDto;
import com.synergy.backend.domain.member.api.dto.resposne.LikedRecruiterResponseDto;
import com.synergy.backend.domain.member.api.dto.resposne.MyInfoResponseDto;
import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.domain.member.service.AttendeeService;
import com.synergy.backend.domain.member.service.RecruiterAttendeeLikeService;
import com.synergy.backend.global.common.ApiResponse;
import com.synergy.backend.global.security.CustomUserDetails;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/attendee")
@RequiredArgsConstructor
public class AttendeeController {

	private final AttendeeService attendeeService;
	private final RecruiterAttendeeLikeService recruiterAttendeeLikeService;

	@PreAuthorize("hasRole('ATTENDEE')")
	@PatchMapping(path = "/onboarding/job-info")
	public ApiResponse<JobInfoResponseDto> addJobInfo(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@Valid @RequestBody JobInfoRequestDto request) {

		String identifier = userDetails.getIdentifier();
		attendeeService.addJobInfo(identifier, request);
		return ApiResponse.ok(null, 200);
	}

	@PreAuthorize("hasRole('ATTENDEE')")
	@PatchMapping(path = "/onboarding/job-info-details")
	public ApiResponse<JobInfoResponseDto> addJobInfoDetails(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@Valid @RequestBody JobInfoDetailsRequestDto request) {

		String identifier = userDetails.getIdentifier();
		attendeeService.addJobInfoDetails(identifier, request);
		return ApiResponse.ok(null, 200);
	}

	@PreAuthorize("hasRole('ATTENDEE')")
	@GetMapping(path = "/my")
	public ApiResponse<MyInfoResponseDto> getMyInformation(
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {
		String identifier = userDetails.getIdentifier();
		return ApiResponse.ok(attendeeService.getMyInformation(identifier), 200);
	}

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

	@PreAuthorize("hasRole('ATTENDEE')")
	@GetMapping("/liked-recruiters")
	public ApiResponse<List<LikedRecruiterResponseDto>> getLikedRecruiters(
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		return ApiResponse.ok(recruiterAttendeeLikeService.getLikedRecruiters(userDetails.getId()), 200);
	}

}
