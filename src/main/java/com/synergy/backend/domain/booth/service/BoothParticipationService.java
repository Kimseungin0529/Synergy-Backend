package com.synergy.backend.domain.booth.service;

import com.synergy.backend.global.common.ApiResponse;

public interface BoothParticipationService {
    ApiResponse<String> participateInBooth(Long attendeeId, Long boothId);
    ApiResponse<String> cancelParticipation(Long attendeeId, Long boothId);
}
