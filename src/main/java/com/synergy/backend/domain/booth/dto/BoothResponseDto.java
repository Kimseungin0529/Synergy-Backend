package com.synergy.backend.domain.booth.dto;

import com.synergy.backend.domain.booth.entity.Booth;

public record BoothResponseDto(
        Long id,
        String name,
        String company,
        String location,
        String description
) {
    public BoothResponseDto(Booth booth) {
        this(
                booth.getId(),
                booth.getName(),
                booth.getCompany(),
                booth.getLocation(),
                booth.getDescription()
        );
    }
}
