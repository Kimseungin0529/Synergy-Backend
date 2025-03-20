package com.synergy.backend.domain.session.dto.sessionparticipateDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record SessionParticipateRateDetailResDto (
        Long sessionId,
        String title,
        LocalDate progressDate,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String fileUrl,
        List<SessionParticipateTechResDto> tech
) {
}
