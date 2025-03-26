package com.synergy.backend.domain.meta.enums;

import java.util.Arrays;
import java.util.List;

import com.synergy.backend.domain.member.entity.details.AgeGroup;
import com.synergy.backend.domain.member.entity.details.BaseAttendeeDetailEnum;
import com.synergy.backend.domain.member.entity.details.ConferenceParticipationPurpose;
import com.synergy.backend.domain.member.entity.details.EducationLevelType;
import com.synergy.backend.domain.member.entity.details.ExperienceLevelType;
import com.synergy.backend.domain.member.entity.details.PreferredCorporateCulture;
import com.synergy.backend.domain.member.entity.details.RegionType;
import com.synergy.backend.domain.member.entity.details.WorkplaceSelectionFactor;

public enum EnumType {
	REGION_TYPE(RegionType.class),
	AGE_GROUPS(AgeGroup.class),
	CONFERENCE_PARTICIPATION_PURPOSE(ConferenceParticipationPurpose.class),
	WORKPLACE_SELECTION_FACTOR(WorkplaceSelectionFactor.class),
	PREFERRED_CORPORATE_CULTURE(PreferredCorporateCulture.class),
	EXPERIENCE_LEVEL_TYPE(ExperienceLevelType.class),
	EDUCATION_LEVELS(EducationLevelType.class);;

	private final Class<? extends
		BaseAttendeeDetailEnum> enumClass;

	EnumType(Class<? extends BaseAttendeeDetailEnum> enumClass) {
		this.enumClass = enumClass;
	}

	public List<EnumResponseDto> getValues() {
		return Arrays.stream(enumClass.getEnumConstants())
			.map(EnumResponseDto::from)
			.toList();
	}
}
