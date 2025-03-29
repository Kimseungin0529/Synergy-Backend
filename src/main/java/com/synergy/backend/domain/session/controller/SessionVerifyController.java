package com.synergy.backend.domain.session.controller;

import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.domain.session.dto.questionDto.QuestionReqDto;
import com.synergy.backend.domain.session.dto.sessionDto.SessionResDto;
import com.synergy.backend.domain.session.service.SessionParticipateService;
import com.synergy.backend.global.annotation.SwaggerSummaryRole;
import com.synergy.backend.global.common.ApiResponse;
import com.synergy.backend.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Session Verify Controller", description = "세션 참여자 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/verify")
public class SessionVerifyController {

    private final SessionParticipateService sessionParticipateService;

    @Operation(
            summary = "세션 참여",
            description = "QR 코드를 활용해 세션에 참여합니다. 세션 ID와 QR 코드로 유효성을 검증합니다."
    )
    @PreAuthorize("hasAnyRole('ATTENDEE')")
    @SwaggerSummaryRole({RoleType.ATTENDEE})
    @PostMapping("/session/{sessionId}")
    public ApiResponse<SessionResDto> verifyQRCode(@AuthenticationPrincipal CustomUserDetails user,
                                                   @PathVariable(name = "sessionId") Long sessionId,
                                                   @RequestParam(name = "qrCode") String qrCode){
        SessionResDto sessionResDto = sessionParticipateService.verifyQRCode(user.getIdentifier(), sessionId, qrCode);

        return ApiResponse.ok(sessionResDto, 200);
    }

    @Operation(
            summary = "세션 Q&A 질문 생성",
            description = "세션 참여자가 세션 ID를 기준으로 질문을 생성합니다."
    )
    @PreAuthorize("hasAnyRole('ATTENDEE')")
    @SwaggerSummaryRole({RoleType.ATTENDEE})
    @PostMapping("/session/{sessionId}/participation")
    public ApiResponse createQuestion(@AuthenticationPrincipal CustomUserDetails user,
                                      @PathVariable(name = "sessionId") Long sessionId,
                                      @RequestBody QuestionReqDto reqDto) {

        sessionParticipateService.createQuestion(user.getIdentifier(), sessionId, reqDto);
        return ApiResponse.ok("Question created successfully!", 200);
    }
}
