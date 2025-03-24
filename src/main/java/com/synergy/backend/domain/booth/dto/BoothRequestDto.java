package com.synergy.backend.domain.booth.dto;

public record BoothRequestDto(
        String companyName,
        String companyType,
        String boothLocation,
        Integer boothNumber,
        String boothDescription,
        byte[] image
) {
}
