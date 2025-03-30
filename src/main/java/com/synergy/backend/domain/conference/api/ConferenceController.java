package com.synergy.backend.domain.conference.api;

import com.synergy.backend.domain.conference.dto.response.ConferenceAttendeeInfoResDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.synergy.backend.domain.conference.dto.requset.ConferenceCreateRequest;
import com.synergy.backend.domain.conference.dto.requset.ConferenceUpdateRequest;
import com.synergy.backend.domain.conference.dto.response.ConferenceCreateResponse;
import com.synergy.backend.domain.conference.dto.response.ConferenceUpdateResponse;
import com.synergy.backend.domain.conference.service.ConferenceService;

import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.global.annotation.SwaggerSummaryRole;
import com.synergy.backend.global.security.CurrentUser;
import com.synergy.backend.global.common.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Conference Controller", description = "컨퍼런스 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/conference")
public class ConferenceController {

	private final ConferenceService conferenceService;

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	@Operation(summary = "컨퍼런스 등록", description = "새로운 컨퍼런스를 생성합니다.")
	@SwaggerSummaryRole({RoleType.ADMIN})
	public ApiResponse<ConferenceCreateResponse> registerConference(@CurrentUser String identifier
		, @RequestBody @Valid ConferenceCreateRequest request) {
		return ApiResponse.ok(conferenceService.registerConference(identifier, request), 201);
	}

	@PatchMapping("/{id}")
	@Operation(summary = "컨퍼런스 수정", description = "기존 컨퍼런스 정보를 수정합니다.")
	@SwaggerSummaryRole({RoleType.ADMIN})
	public ApiResponse<ConferenceUpdateResponse> updateConference(@CurrentUser String identifier,
		@PathVariable(name = "id") Long id, @RequestBody @Valid ConferenceUpdateRequest request) {
		return ApiResponse.ok(conferenceService.updateConference(identifier, id, request), 200);
	}

	@GetMapping("/{id}")
	@Operation(summary = "컨퍼런스 대시보드 참여자 세부 조회", description = "세션/부스/컨퍼런스 참여자들을 조회합니다.")
	@SwaggerSummaryRole({RoleType.ADMIN})
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<ConferenceAttendeeInfoResDto> getAttendeeInfo(
			@CurrentUser String identifier,
			@PathVariable(name = "id") Long id){

		return ApiResponse.ok(conferenceService.findConferenceAttendeeInfo(identifier, id), 200);
	}
}
