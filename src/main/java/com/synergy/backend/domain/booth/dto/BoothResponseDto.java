package com.synergy.backend.domain.booth.dto;

import com.synergy.backend.domain.booth.entity.Booth;

public record BoothResponseDto(
        Long id,
        String companyName,
        String companyType,
        String boothLocation,
        String boothNumber,
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
                booth.getBoothDescription(),
                booth.getQrUrl(),
                booth.getImageUrl()
        );
    }
}
