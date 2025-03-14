package com.synergy.backend.domain.session.dto.sessionparticipate;

import com.synergy.backend.domain.session.dto.SessionResDto;

import java.time.LocalDate;
import java.util.List;

public record SessionParticipateRateResDto(
        LocalDate progressDate,
        List<SessionParticipateRateDetailResDto> dtoList
) {
}
