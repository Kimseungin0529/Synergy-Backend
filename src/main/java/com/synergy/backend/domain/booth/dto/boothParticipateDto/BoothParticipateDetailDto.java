package com.synergy.backend.domain.booth.dto.boothParticipateDto;

public record BoothParticipateDetailDto(
        Long boothId,
        String attendeePercent,
        String companyName
) {
}
