package com.synergy.backend.domain.member.api.dto;


import lombok.Builder;

import java.util.List;

@Builder
public record AttendeeFilterRequest(
        List<String> occupations,
        String educationLevel,
        String ageGroup,
        List<String> regions
) {
    public static AttendeeFilterRequest of(List<String> occupations, String educationLevel, String ageGroup, List<String> regions) {
        return AttendeeFilterRequest.builder()
                .occupations(occupations)
                .educationLevel(educationLevel)
                .ageGroup(ageGroup)
                .regions(regions)
                .build();
    }
}
