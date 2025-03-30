package com.synergy.backend.domain.booth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.synergy.backend.domain.booth.entity.Booth;

import java.time.LocalDate;

public record BoothDetailResponseDto(
        Long id,
        String companyName,
        String companyType,
        String boothLocation,
        String boothNumber,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate progressDate,
        String boothDescription,
        String imageUrl,
        Boolean isQRVerify
) {
    public BoothDetailResponseDto(Booth booth, Boolean isQRVerify) {
        this(
                booth.getId(),
                booth.getCompanyName(),
                booth.getCompanyType(),
                booth.getBoothLocation(),
                booth.getBoothNumber(),
                booth.getProgressDate(),
                booth.getBoothDescription(),
                booth.getImageUrl(),
                isQRVerify
        );
    }
}
