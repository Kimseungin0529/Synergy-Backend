package com.synergy.backend.domain.booth.dto;

public record BoothRequestDto(
        String name,
        String company,
        String location,
        String description
) {
}
