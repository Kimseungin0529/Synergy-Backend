package com.synergy.backend.domain.member.api.dto.resposne;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.details.BaseAttendeeDetailEnum;

public record AttendeeDetailInfoResponseDto(
	String education,
	String ageGroup,
	String techStacks,
	Set<String> desiredWorkRegion,
	String selfIntroduction,
	String information,
	Set<String> workplaceSelectionFactors,
	Set<String> preferredCorporateCultures
) {
	public static AttendeeDetailInfoResponseDto from(Attendee attendee) {
		return new AttendeeDetailInfoResponseDto(
			attendee.getEducationLevel() != null ? attendee.getEducationLevel().getDescription() : "",
			attendee.getAgeGroup() != null ? attendee.getAgeGroup().getDescription() : "",
			attendee.getTechStacks() != null ? attendee.getTechStacks() : "",
			convertEnumSetToDescriptions(attendee.getDesiredWorkRegion()),
			attendee.getSelfIntroduction() != null ? attendee.getSelfIntroduction() : "N",
			attendee.getInformation() != null ? attendee.getInformation() : "",
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
