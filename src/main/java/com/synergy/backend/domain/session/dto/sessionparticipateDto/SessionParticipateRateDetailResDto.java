package com.synergy.backend.domain.session.dto.sessionparticipateDto;

public record SessionParticipateRateDetailResDto (
        String sessionId,
        String title,
        int currentCount,
        int maxCount
) {
}
