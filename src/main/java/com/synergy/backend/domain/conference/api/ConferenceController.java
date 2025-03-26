package com.synergy.backend.domain.conference.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.synergy.backend.domain.conference.dto.requset.ConferenceCreateRequest;
import com.synergy.backend.domain.conference.dto.requset.ConferenceUpdateRequest;
import com.synergy.backend.domain.conference.dto.response.ConferenceCreateResponse;
import com.synergy.backend.domain.conference.dto.response.ConferenceUpdateResponse;
import com.synergy.backend.domain.conference.service.ConferenceService;

import com.synergy.backend.global.security.CurrentUser;
import com.synergy.backend.global.common.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Conference Controller", description = "컨퍼런스 관련 API")
@RestController @Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/conference")
public class ConferenceController {

	private final ConferenceService conferenceService;

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	@Operation(summary = "컨퍼런스 등록", description = "새로운 컨퍼런스를 생성합니다.")
	public ApiResponse<ConferenceCreateResponse> registerConference(@CurrentUser String identifier
		, @RequestBody @Valid ConferenceCreateRequest request) {
		return ApiResponse.ok(conferenceService.registerConference(identifier, request), 201);
	}

	@PatchMapping("/{id}")
	@Operation(summary = "컨퍼런스 수정", description = "기존 컨퍼런스 정보를 수정합니다.")
	public ApiResponse<ConferenceUpdateResponse> updateConference(@CurrentUser String identifier,
		@PathVariable(name = "id") Long id, @RequestBody @Valid ConferenceUpdateRequest request) {
		return ApiResponse.ok(conferenceService.updateConference(identifier, id, request), 200);
	}
}
