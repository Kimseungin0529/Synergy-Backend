package com.synergy.backend.domain.member.api.dto.request;

import java.util.List;

import lombok.Builder;

@Builder
public record AttendeeFilterRequest(
	List<String> desiredJobPositions,
	String educationLevel,
	String ageGroup,
	String experienceLevel,
	List<String> regions
) {
	public static AttendeeFilterRequest of(List<String> desiredJobPositions, String educationLevel, String ageGroup,
		String experienceLevel, List<String> regions) {
		return AttendeeFilterRequest.builder()
			.desiredJobPositions(desiredJobPositions)
			.educationLevel(educationLevel)
			.ageGroup(ageGroup)
			.experienceLevel(experienceLevel)
			.regions(regions)
			.build();
	}
}
