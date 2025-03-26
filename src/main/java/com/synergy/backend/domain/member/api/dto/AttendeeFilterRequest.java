package com.synergy.backend.domain.member.api.dto;


import lombok.Builder;

import java.util.List;

@Builder
public record AttendeeFilterRequest(
        List<String> desiredOccupations,
        String educationLevel,
        String ageGroup,
        String experienceLevel,
        List<String> regions
) {
    public static AttendeeFilterRequest of(List<String> desiredOccupations, String educationLevel, String ageGroup, String experienceLevel, List<String> regions) {
        return AttendeeFilterRequest.builder()
                .desiredOccupations(desiredOccupations)
                .educationLevel(educationLevel)
                .ageGroup(ageGroup)
                .experienceLevel(experienceLevel)
                .regions(regions)
                .build();
    }
}
