package com.synergy.backend.domain.member.service;

import java.util.Set;
import java.util.stream.Collectors;

import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.details.PreferredCorporateCulture;
import com.synergy.backend.domain.member.entity.details.RegionType;
import com.synergy.backend.domain.member.entity.details.WorkplaceSelectionFactor;

import lombok.Builder;

@Builder
public record AttendeeDetailResponse(
	String name,
	String occupation,
	String experienceLevel,
	String educationLevel,
	String ageGroup,
	String techStacks,
	String selfIntroduction,
	String information,
	Set<String> regionTypeCandidates,
	Set<String> workplaceSelectionFactors,
	Set<String> preferredCorporateCultures
) {

	public static AttendeeDetailResponse of(Attendee attendee) {
		return AttendeeDetailResponse.builder()
			.name(attendee.getName())
			.occupation(attendee.getCurrentJobPosition().getName())
			.experienceLevel(attendee.getExperienceLevel().getDescription())
			.educationLevel(attendee.getEducationLevel().getDescription())
			.ageGroup(attendee.getAgeGroup().getDescription())
			.techStacks(attendee.getTechStacks())
			.selfIntroduction(attendee.getSelfIntroduction())
			.information(attendee.getInformation())
			.regionTypeCandidates(
				attendee.getDesiredWorkRegion().stream().map(RegionType::getDescription).collect(Collectors.toSet()))
			.workplaceSelectionFactors(attendee.getWorkplaceSelectionFactors()
				.stream()
				.map(WorkplaceSelectionFactor::getDescription)
				.collect(Collectors.toSet()))
			.preferredCorporateCultures(attendee.getPreferredCorporateCultures()
				.stream()
				.map(PreferredCorporateCulture::getDescription)
				.collect(Collectors.toSet()))
			.build();
	}
}
