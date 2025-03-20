package com.synergy.backend.domain.session.dto.sessionparticipateDto;

public record SessionParticipateRateResDto(
        String title,
        Long currentAttendee,
        Integer maximumAttendee
) {
}
