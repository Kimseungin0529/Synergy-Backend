package com.synergy.backend.domain.booth.dto;

import com.synergy.backend.domain.booth.entity.Booth;

public record BoothResponseDto(
        Long id,
        String companyName,
        String companyType,
        String location,
        String detailLocation,
        String image
) {

    public static BoothResponseDto from(Booth booth) {
        return new BoothResponseDto(
                booth.getId(),
                booth.getCompanyName(),
                booth.getBoothNumber(),
                booth.getBoothLocation(),
                booth.getBoothNumber(),
                booth.getImageUrl()
        );
    }

}
