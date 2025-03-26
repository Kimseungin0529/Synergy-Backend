package com.synergy.backend.domain.session.controller;

import com.synergy.backend.domain.session.dto.questionDto.QuestionReqDto;
import com.synergy.backend.domain.session.dto.sessionDto.SessionResDto;
import com.synergy.backend.domain.session.service.SessionParticipateService;
import com.synergy.backend.global.common.ApiResponse;
import com.synergy.backend.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/verify")
public class SessionVerifyController {

    private final SessionParticipateService sessionParticipateService;

    @PreAuthorize("hasAnyRole('ATTENDEE')")
    @PostMapping("/session")
    public ApiResponse<SessionResDto> verifyQRCode(@AuthenticationPrincipal CustomUserDetails user,
                                                   @RequestParam(name = "qrCode") String qrCode){
        SessionResDto sessionResDto = sessionParticipateService.verifyQRCode(user.getIdentifier(), qrCode);

        return ApiResponse.ok(sessionResDto, 200);
    }

    @PreAuthorize("hasAnyRole('ATTENDEE')")
    @PostMapping("/conference/{conferenceId}/session/{sessionId}/participation")
    public ApiResponse createQuestion(@AuthenticationPrincipal CustomUserDetails user,
                                      @PathVariable(name = "conferenceId") Long conferenceId,
                                      @PathVariable(name = "sessionId") Long sessionId,
                                      @RequestBody QuestionReqDto reqDto) {

        sessionParticipateService.createQuestion(user.getIdentifier(), conferenceId, sessionId, reqDto);
        return ApiResponse.ok("Question created successfully!", 200);
    }
}
