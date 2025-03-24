package com.synergy.backend.domain.booth.controller;

import com.synergy.backend.domain.booth.service.BoothParticipationService;
import com.synergy.backend.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/verify")
public class BoothVerifyController {

    private final BoothParticipationService boothParticipationService;

    @PostMapping("/{boothId}/participate/{attendeeId}")
    public ApiResponse<String> participateInBooth(
            @PathVariable Long conferenceId,
            @PathVariable Long boothId,
            @PathVariable Long attendeeId) {
        boothParticipationService.participateInBooth(attendeeId, boothId);
        return ApiResponse.ok("부스 참여가 완료되었습니다.", 201);
    }

    @DeleteMapping("/{boothId}/cancel/{attendeeId}")
    public ApiResponse<String> cancelParticipation(
            @PathVariable Long conferenceId,
            @PathVariable Long boothId,
            @PathVariable Long attendeeId) {
        boothParticipationService.cancelParticipation(attendeeId, boothId);
        return ApiResponse.ok("부스 참여가 취소되었습니다.", 200);
    }
}
