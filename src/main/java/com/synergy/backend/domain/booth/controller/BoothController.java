package com.synergy.backend.domain.booth.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.synergy.backend.domain.booth.dto.BoothRequestDto;
import com.synergy.backend.domain.booth.dto.BoothResponseDto;
import com.synergy.backend.domain.booth.service.BoothService;
import com.synergy.backend.global.common.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Booth Controller", description = "부스 관련 API")
@RestController
@RequestMapping("/api/v1/conference/{conferenceId}/booths")
@RequiredArgsConstructor
public class BoothController {

	private final BoothService boothService;

	@Operation(summary = "부스 단건 조회", description = "ID를 통해 특정 부스를 조회합니다.")
	@GetMapping("/{id}")
	public ApiResponse<BoothResponseDto> getBoothById(
		@PathVariable Long conferenceId,
		@PathVariable Long id
	) {
		return ApiResponse.ok(boothService.getBoothById(conferenceId, id), 200);
	}

	@Operation(summary = "전체 부스 목록 조회", description = "해당 컨퍼런스의 전체 부스 목록을 페이지네이션으로 조회합니다.")
	@GetMapping
	public ApiResponse<Page<BoothResponseDto>> getAllBooths(
		@PathVariable Long conferenceId,
		Pageable pageable
	) {
		return ApiResponse.ok(boothService.getAllBooths(conferenceId, pageable), 200);
	}

	@Operation(summary = "부스 생성", description = "해당 컨퍼런스에 새로운 부스를 생성합니다.")
	@PostMapping
	public ApiResponse<BoothResponseDto> createBooth(
		@PathVariable Long conferenceId,
		@RequestBody BoothRequestDto request
	) {
		return ApiResponse.ok(boothService.createBooth(conferenceId, request), 201);
	}

	@Operation(summary = "부스 정보 수정", description = "부스 ID를 기준으로 부스 정보를 수정합니다.")
	@PutMapping("/{id}")
	public ApiResponse<BoothResponseDto> updateBooth(
		@PathVariable Long conferenceId,
		@PathVariable Long id,
		@RequestBody BoothRequestDto request
	) {
		return ApiResponse.ok(boothService.updateBooth(conferenceId, id, request), 200);
	}

	@Operation(summary = "부스 삭제", description = "부스 ID를 기준으로 해당 부스를 삭제합니다.")
	@DeleteMapping("/{id}")
	public ApiResponse<Void> deleteBooth(
		@PathVariable Long conferenceId,
		@PathVariable Long id
	) {
		boothService.deleteBooth(conferenceId, id);
		return ApiResponse.ok(null, 204);
	}
}
