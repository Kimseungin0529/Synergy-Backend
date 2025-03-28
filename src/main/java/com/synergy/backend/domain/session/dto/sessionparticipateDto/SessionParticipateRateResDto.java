package com.synergy.backend.domain.session.dto.sessionparticipateDto;

import java.time.LocalDate;

public record SessionParticipateRateResDto(
        String title,
        LocalDate currentDate,
        Long currentAttendee,
        Integer maximumAttendee
) {
}
