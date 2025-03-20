package com.synergy.backend.domain.member.api.dto.resposne;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.details.BaseAttendeeDetailEnum;

public record AttendeeInfoDetailResponseDto(
	String name,
	String jobName,
	String experience,
	String education,
	String ageGroup,
	String techStacks,
	Set<String> desiredWorkRegion,
	String selfIntroduction,
	String information,
	Set<String> workplaceSelectionFactors,
	Set<String> preferredCorporateCultures
) {
	public static AttendeeInfoDetailResponseDto from(Attendee attendee) {
		return new AttendeeInfoDetailResponseDto(
			attendee.getName() != null ? attendee.getName() : "Unknown",
			attendee.getCurrentJobCategory() != null ? attendee.getCurrentJobCategory().getName() : "Unknown",
			attendee.getExperienceLevel() != null ? attendee.getExperienceLevel().getDescription() : "Unknown",
			attendee.getEducationLevel() != null ? attendee.getEducationLevel().getDescription() : "Unknown",
			attendee.getAgeGroup() != null ? attendee.getAgeGroup().getDescription() : "Unknown",
			attendee.getTechStacks() != null ? attendee.getTechStacks() : "No tech stack specified",
			convertEnumSetToDescriptions(attendee.getDesiredWorkRegion()),
			attendee.getSelfIntroduction() != null ? attendee.getSelfIntroduction() : "No self introduction",
			attendee.getInformation() != null ? attendee.getInformation() : "No additional information",
			convertEnumSetToDescriptions(attendee.getWorkplaceSelectionFactors()),
			convertEnumSetToDescriptions(attendee.getPreferredCorporateCultures())
		);
	}

	private static Set<String> convertEnumSetToDescriptions(Set<? extends BaseAttendeeDetailEnum> enums) {
		return (enums != null) ?
			enums.stream().map(BaseAttendeeDetailEnum::getDescription).collect(Collectors.toSet()) :
			Collections.emptySet();
	}
}
