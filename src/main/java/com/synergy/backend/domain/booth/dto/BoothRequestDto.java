package com.synergy.backend.domain.booth.dto;

import java.time.LocalDate;

public record BoothRequestDto(
        String companyName,
        String companyType,
        LocalDate progressDate,
        String boothLocation,
        String boothNumber,
        String boothDescription
) {
}
