package com.synergy.backend.domain.member.api;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.synergy.backend.domain.member.api.dto.request.InterestRequestDto;
import com.synergy.backend.domain.member.api.dto.resposne.InterestResponseDto;
import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.domain.member.exception.AccessDeniedException;
import com.synergy.backend.domain.member.service.AttendeeServiceImpl;
import com.synergy.backend.global.common.ApiResponse;
import com.synergy.backend.global.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/interest")
@RequiredArgsConstructor
public class AttendeeController {

	private final AttendeeServiceImpl attendeeService;

	@PatchMapping
	public ApiResponse<?> addUserInterest(@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestBody InterestRequestDto request) {

		String identifier = userDetails.getIdentifier();
		RoleType role = userDetails.getRole();

		if (role != RoleType.ATTENDEE) {
			throw new AccessDeniedException();
		}

		return ApiResponse.ok(InterestResponseDto.from(attendeeService.addInterests(identifier, request.interestIds())),
			200);
	}
}
