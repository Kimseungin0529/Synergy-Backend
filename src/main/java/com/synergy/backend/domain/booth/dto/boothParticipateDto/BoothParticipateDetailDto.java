package com.synergy.backend.domain.booth.dto.boothParticipateDto;

public record BoothParticipateDetailDto(
        Long boothId,
        Long totalCount,
        Long attendeeCount,
        String companyName
) {
}
