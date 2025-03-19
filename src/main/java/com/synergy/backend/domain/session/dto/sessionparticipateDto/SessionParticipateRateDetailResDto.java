package com.synergy.backend.domain.session.dto.sessionparticipateDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SessionParticipateRateDetailResDto (
        String title,
        LocalDate progressDate,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
