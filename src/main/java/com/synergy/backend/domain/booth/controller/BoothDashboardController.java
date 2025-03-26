package com.synergy.backend.domain.booth.controller;

import com.synergy.backend.domain.booth.dto.InterestParticipationDto;
import com.synergy.backend.domain.booth.service.BoothParticipationService;
import com.synergy.backend.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dashboard/conference/{conferenceId}")
@RequiredArgsConstructor
public class BoothDashboardController {

    private final BoothParticipationService boothParticipationService;

    @GetMapping("/booths/{boothId}/participation/interest")
    public ApiResponse<List<InterestParticipationDto>> getParticipationCountByInterest(
            @PathVariable Long conferenceId,
            @PathVariable Long boothId) {
        List<InterestParticipationDto> result = boothParticipationService.getParticipationCountByInterest(boothId);
        return ApiResponse.ok(result, 200);
    }
}
