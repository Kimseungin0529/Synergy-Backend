package com.synergy.backend.domain.booth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.synergy.backend.domain.booth.entity.Booth;

import java.time.LocalDate;

public record BoothResponseDto(
        Long id,
        String companyName,
        String companyType,
        String boothLocation,
        String boothNumber,
        @JsonFormat(pattern = "MM-dd")
        LocalDate progressDate,
        String boothDescription,
        String qrUrl,
        String imageUrl
) {
    public BoothResponseDto(Booth booth) {
        this(
                booth.getId(),
                booth.getCompanyName(),
                booth.getCompanyType(),
                booth.getBoothLocation(),
                booth.getBoothNumber(),
                booth.getProgressDate(),
                booth.getBoothDescription(),
                booth.getQrUrl(),
                booth.getImageUrl()
        );
    }
}
