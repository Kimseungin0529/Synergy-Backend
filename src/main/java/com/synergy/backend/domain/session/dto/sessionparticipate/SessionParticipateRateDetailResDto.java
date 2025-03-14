package com.synergy.backend.domain.session.dto.sessionparticipate;

public record SessionParticipateRateDetailResDto (
        String sessionId,
        String title,
        int currentCount,
        int maxCount
) {
}
