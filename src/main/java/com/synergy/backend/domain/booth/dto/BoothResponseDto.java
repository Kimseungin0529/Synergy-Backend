package com.synergy.backend.domain.booth.dto;

import com.synergy.backend.domain.booth.entity.Booth;

public record BoothResponseDto(
        Long id,
        String companyName,
        String companyType,
        String boothLocation,
        Integer boothNumber,
        String boothDescription,
        byte[] image
) {
    public BoothResponseDto(Booth booth) {
        this(
                booth.getId(),
                booth.getCompanyName(),
                booth.getCompanyType(),
                booth.getBoothLocation(),
                booth.getBoothNumber(),
                booth.getBoothDescription(),
                booth.getImage()
        );
    }
}
