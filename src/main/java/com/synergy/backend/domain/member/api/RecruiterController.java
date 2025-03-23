package com.synergy.backend.domain.member.api;

import java.util.List;

import com.synergy.backend.domain.member.api.dto.AttendeeFilterRequest;
import com.synergy.backend.domain.member.api.dto.AttendeeListResponse;
import com.synergy.backend.domain.member.service.AttendeeDetailResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.synergy.backend.domain.member.api.dto.resposne.LikedAttendeeResponseDto;
import com.synergy.backend.domain.member.api.dto.resposne.RecruiterMyInfoResponseDto;
import com.synergy.backend.domain.member.service.RecruiterAttendeeLikeService;
import com.synergy.backend.domain.member.service.RecruiterService;
import com.synergy.backend.global.common.ApiResponse;
import com.synergy.backend.global.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/recruiter")
@RequiredArgsConstructor
public class RecruiterController {

	private final RecruiterService recruiterService;
	private final RecruiterAttendeeLikeService recruiterAttendeeLikeService;

	@PreAuthorize("hasRole('RECRUITER')")
	@GetMapping("/my")
	public ApiResponse<RecruiterMyInfoResponseDto> getMyInformation(
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		return ApiResponse.ok(recruiterService.getMyInformation(userDetails.getId()), 200);
	}

	@PreAuthorize("hasRole('RECRUITER')")
	@PostMapping("/attendees/{attendeeId}/like")
	public ApiResponse<?> likeAttendee(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable(value = "attendeeId") Long attendeeId
	) {
		recruiterAttendeeLikeService.likeAttendee(userDetails.getId(), attendeeId);
		return ApiResponse.ok(null, 200);
	}

	@PreAuthorize("hasRole('RECRUITER')")
	@DeleteMapping("/attendees/{attendeeId}/unlike")
	public ApiResponse<?> unlikeAttendee(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable(value = "attendeeId") Long attendeeId
	) {
		recruiterAttendeeLikeService.unlikeAttendee(userDetails.getId(), attendeeId);
		return ApiResponse.ok(null, 200);
	}

	@PreAuthorize("hasRole('RECRUITER')")
	@GetMapping("/me/liked-attendees")
	public ApiResponse<List<LikedAttendeeResponseDto>> getLikedAttendees(
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		return ApiResponse.ok(recruiterAttendeeLikeService.getLikedAttendees(userDetails.getId()), 200);
	}

	@PreAuthorize("hasRole('RECRUITER')")
	@GetMapping("/attendee/{id}")
	public ApiResponse<AttendeeDetailResponse> getAttendee(@PathVariable("id") Long id) {
		return ApiResponse.ok(recruiterService.getAttendeeFrom(id), 200);
	}

	@PreAuthorize("hasRole('RECRUITER')")
	@GetMapping("/{id}/attendees")
	public ApiResponse<AttendeeListResponse> getAttendees(@PageableDefault(page = 0, size = 20)
														  Pageable pageable,
														  @PathVariable("id") Long recruiterId,
														  @RequestParam(required = false) List<String> occupations,
														  @RequestParam(required = false) String EducationLevel,
														  @RequestParam(required = false) String ageGroup,
														  @RequestParam(required = false) String experienceLevel,
														  @RequestParam(required = false) List<String> regions
														  ) {

		AttendeeFilterRequest requestCondition = AttendeeFilterRequest.of(occupations, EducationLevel, ageGroup, experienceLevel, regions);
		return ApiResponse.ok(recruiterService.getAttendeesBy(pageable, recruiterId, requestCondition), 200);
	}


}
