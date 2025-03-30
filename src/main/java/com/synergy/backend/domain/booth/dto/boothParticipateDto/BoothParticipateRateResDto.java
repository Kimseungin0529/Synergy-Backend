package com.synergy.backend.domain.booth.dto.boothParticipateDto;

import java.time.LocalDate;
import java.util.List;

public record BoothParticipateRateResDto(
        LocalDate currentDate,
        List<BoothParticipateDetailDto> boothParticipateDetailDtoList
) {
}
