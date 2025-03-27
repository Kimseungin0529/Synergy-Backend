package com.synergy.backend.domain.booth.controller;

import com.google.zxing.WriterException;
import com.synergy.backend.domain.booth.dto.BoothRequestDto;
import com.synergy.backend.domain.booth.dto.BoothDetailResponseDto;
import com.synergy.backend.domain.booth.dto.BoothResponseDto;
import com.synergy.backend.domain.booth.service.BoothService;
import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.global.annotation.SwaggerSummaryRole;
import com.synergy.backend.global.common.ApiResponse;
import com.synergy.backend.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Booth Controller", description = "부스 관련 API")
@RestController
@RequestMapping("/api/v1/conference/{conferenceId}/booths")
@RequiredArgsConstructor
public class BoothController {

    private final BoothService boothService;

	@SwaggerSummaryRole({RoleType.ADMIN, RoleType.RECRUITER, RoleType.ATTENDEE})
	@PreAuthorize("hasAnyRole('ADMIN', 'ATTENDEE', 'RECRUITER')")
    @Operation(summary = "부스 단건 조회", description = "ID를 통해 특정 부스를 조회합니다.")
    @GetMapping("/{id}")
    public ApiResponse<BoothDetailResponseDto> getBoothById(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long conferenceId,
            @PathVariable Long id
    ) {

        return ApiResponse.ok(boothService.getBoothById(conferenceId, id), 200);
    }

	@SwaggerSummaryRole({RoleType.ADMIN, RoleType.RECRUITER, RoleType.ATTENDEE})@PreAuthorize("hasAnyRole('ADMIN', 'ATTENDEE', 'RECRUITER')")
    @Operation(summary = "전체 부스 목록 조회", description = "해당 컨퍼런스의 전체 부스 목록을 페이지네이션으로 조회합니다.")
    @GetMapping
    public ApiResponse<Page<BoothResponseDto>> getAllBooths(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long conferenceId,
            @ParameterObject Pageable pageable
    ) {
        return ApiResponse.ok(boothService.getAllBooths(conferenceId, pageable), 200);
    }

	@SwaggerSummaryRole({RoleType.ADMIN})
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "부스 생성", description = "해당 컨퍼런스에 새로운 부스를 생성합니다.")
    @PostMapping
    public ApiResponse<BoothDetailResponseDto> createBooth(
            @AuthenticationPrincipal CustomUserDetails user,
            HttpServletRequest request,
            @PathVariable Long conferenceId,
            @RequestPart BoothRequestDto requestDto,
            @RequestPart MultipartFile imageFile
    ) throws WriterException {

        String router = request.getHeader("Origin");
        return ApiResponse.ok(boothService.createBooth(conferenceId, router, requestDto, imageFile), 201);
    }

	@SwaggerSummaryRole({RoleType.ADMIN})
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "부스 정보 수정", description = "부스 ID를 기준으로 부스 정보를 수정합니다.")
    @PutMapping("/{id}")
    public ApiResponse<BoothDetailResponseDto> updateBooth(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long conferenceId,
            @PathVariable Long id,
            @RequestPart BoothRequestDto request,
            @RequestPart(required = false) MultipartFile imageFile
    ) {
        return ApiResponse.ok(boothService.updateBooth(conferenceId, id, request, imageFile), 200);
    }

	@SwaggerSummaryRole({RoleType.ADMIN})
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "부스 삭제", description = "부스 ID를 기준으로 해당 부스를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteBooth(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long conferenceId,
            @PathVariable Long id
    ) {
        boothService.deleteBooth(conferenceId, id);
        return ApiResponse.ok(null, 204);
    }
}
