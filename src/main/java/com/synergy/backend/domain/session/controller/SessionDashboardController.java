package com.synergy.backend.domain.session.controller;

import com.synergy.backend.domain.session.dto.sessionparticipateDto.SessionParticipateRateDetailResDto;
import com.synergy.backend.domain.session.dto.sessionparticipateDto.SessionParticipateRateResDto;
import com.synergy.backend.domain.session.service.SessionParticipateService;
import com.synergy.backend.global.common.ApiResponse;
import com.synergy.backend.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dashboard/conference/{conferenceId}")
public class SessionDashboardController {

    private final SessionParticipateService sessionParticipateService;

    @GetMapping
    public ApiResponse<List<SessionParticipateRateResDto>> getSessionParticipateRates(@AuthenticationPrincipal CustomUserDetails user,
                                                                                      @PathVariable Long conferenceId) {

        return ApiResponse.ok(sessionParticipateService.getSessionParticipateRate(user.getIdentifier(), conferenceId), 200);
    }

    @GetMapping("/detail")
    public ApiResponse<List<SessionParticipateRateDetailResDto>> getSessionParticipateRateDetails(@AuthenticationPrincipal CustomUserDetails user,
                                                                                                  @PathVariable Long conferenceId) {

        return ApiResponse.ok(sessionParticipateService.getSessionParticipateRateDetail(user.getIdentifier(), conferenceId), 200);
    }
}
