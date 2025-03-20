package com.synergy.backend.domain.member.api;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.synergy.backend.domain.member.service.RecruiterLikeService;
import com.synergy.backend.global.common.ApiResponse;
import com.synergy.backend.global.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/recruiter")
@RequiredArgsConstructor
public class RecruiterController {

	private final RecruiterLikeService recruiterLikeService;

	@PreAuthorize("hasRole('RECRUITER')")
	@PostMapping("/attendees/{attendeeId}/like")
	public ApiResponse<?> likeAttendee(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable(value = "attendeeId") Long attendeeId
	) {
		recruiterLikeService.likeAttendee(userDetails.getId(), attendeeId);
		return ApiResponse.ok(null, 200);
	}

	@PreAuthorize("hasRole('RECRUITER')")
	@DeleteMapping("/attendees/{attendeeId}/unlike")
	public ApiResponse<?> unlikeAttendee(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable(value = "attendeeId") Long attendeeId
	) {
		recruiterLikeService.unlikeAttendee(userDetails.getId(), attendeeId);
		return ApiResponse.ok(null, 200);
	}
}
