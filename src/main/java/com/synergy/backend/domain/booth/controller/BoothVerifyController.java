package com.synergy.backend.domain.booth.controller;

import com.synergy.backend.domain.booth.dto.BoothResponseDto;
import com.synergy.backend.domain.booth.service.BoothParticipationService;
import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.domain.session.dto.sessionDto.SessionResDto;
import com.synergy.backend.global.annotation.SwaggerSummaryRole;
import com.synergy.backend.global.common.ApiResponse;
import com.synergy.backend.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Booth Verify Controller", description = "부스 참여자 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/verify")
public class BoothVerifyController {

    private final BoothParticipationService boothParticipationService;

    @Operation(
            summary = "부스 참여",
            description = "QR 코드를 활용해 부스에 참여합니다. 부스 ID와 QR 코드로 유효성을 검증합니다."
    )
    @SwaggerSummaryRole({RoleType.ATTENDEE})
    @PreAuthorize("hasAnyRole('ATTENDEE')")
    @PostMapping("/booth/{boothId}")
    public ApiResponse<BoothResponseDto> participateInBooth(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable(name = "boothId") Long boothId,
            @RequestParam(name = "qrCode") String qrCode) {

        return ApiResponse.ok(boothParticipationService.participateInBooth(user.getIdentifier(), boothId, qrCode), 201);
    }

}
