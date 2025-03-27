package com.synergy.backend.domain.booth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record BoothRequestDto(
        String companyName,
        String companyType,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate progressDate,
        String boothLocation,
        String boothNumber,
        String boothDescription
) {
}
