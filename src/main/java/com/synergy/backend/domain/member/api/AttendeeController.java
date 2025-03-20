package com.synergy.backend.domain.member.api;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.synergy.backend.domain.member.api.dto.request.InterestRequestDto;
import com.synergy.backend.domain.member.api.dto.request.JobInfoDetailsRequestDto;
import com.synergy.backend.domain.member.api.dto.request.JobInfoRequestDto;
import com.synergy.backend.domain.member.api.dto.resposne.AttendeeInfoDetailResponseDto;
import com.synergy.backend.domain.member.api.dto.resposne.InterestResponseDto;
import com.synergy.backend.domain.member.api.dto.resposne.JobInfoResponseDto;
import com.synergy.backend.domain.member.api.dto.resposne.MyInfoResponseDto;
import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.domain.member.exception.AccessDeniedException;
import com.synergy.backend.domain.member.service.AttendeeService;
import com.synergy.backend.global.common.ApiResponse;
import com.synergy.backend.global.security.CustomUserDetails;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/attendee")
@RequiredArgsConstructor
public class AttendeeController {

	private final AttendeeService attendeeService;

	@PatchMapping(path = "/onboarding/interest")
	public ApiResponse<InterestResponseDto> addUserInterest(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@Valid @RequestBody InterestRequestDto request) {

		String identifier = userDetails.getIdentifier();
		RoleType role = userDetails.getRole();

		if (role != RoleType.ATTENDEE) {
			throw new AccessDeniedException();
		}

		return ApiResponse.ok(
			InterestResponseDto.from(attendeeService.addInterests(identifier, request.interestCodes())),
			200);
	}

	@PatchMapping(path = "/onboarding/job-info")
	public ApiResponse<JobInfoResponseDto> addJobInfo(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@Valid @RequestBody JobInfoRequestDto request) {

		String identifier = userDetails.getIdentifier();
		RoleType role = userDetails.getRole();

		if (role != RoleType.ATTENDEE) {
			throw new AccessDeniedException();
		}
		attendeeService.addJobInfo(identifier, request);
		return ApiResponse.ok(null, 200);
	}

	@PatchMapping(path = "/onboarding/job-info-details")
	public ApiResponse<JobInfoResponseDto> addJobInfoDetails(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@Valid @RequestBody JobInfoDetailsRequestDto request) {

		String identifier = userDetails.getIdentifier();
		RoleType role = userDetails.getRole();

		if (role != RoleType.ATTENDEE) {
			throw new AccessDeniedException();
		}
		attendeeService.addJobInfoDetails(identifier, request);
		return ApiResponse.ok(null, 200);
	}

	@GetMapping(path = "/my")
	public ApiResponse<MyInfoResponseDto> getMyInformation(
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {
		String identifier = userDetails.getIdentifier();
		RoleType role = userDetails.getRole();

		if (role != RoleType.ATTENDEE) {
			throw new AccessDeniedException();
		}

		return ApiResponse.ok(attendeeService.getMyInformation(identifier), 200);
	}

	@GetMapping(path = "/{attendeeId}")
	public ApiResponse<AttendeeInfoDetailResponseDto> getAttendeeInfoDetail(
		@PathVariable Long attendeeId,
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {
		String identifier = userDetails.getIdentifier();
		RoleType role = userDetails.getRole();
		return ApiResponse.ok(attendeeService.getAttendeeInfoDetail(attendeeId, identifier, role), 200);
	}

}
