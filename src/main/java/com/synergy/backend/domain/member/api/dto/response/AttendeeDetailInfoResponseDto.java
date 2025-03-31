package com.synergy.backend.domain.member.api.dto.response;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.details.BaseAttendeeDetailEnum;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "참가자 상세 정보 응답 DTO")
public record AttendeeDetailInfoResponseDto(
	@Schema(description = "학력", example = "4년제 대학 졸업")
	String education,

	@Schema(description = "연령대", example = "20~24세 이하")
	String ageGroup,

	@Schema(description = "보유 기술", example = "Java, AWS, Spring Boot")
	String techStacks,

	@Schema(description = "희망 근무 지역 리스트", example = "수도권")
	Set<String> desiredWorkRegion,

	@Schema(description = "자기소개")
	String selfIntroduction,

	@Schema(description = "경험 및 기타 정보")
	String information,

	@Schema(description = "직장 선택 요소 리스트")
	Set<String> workplaceSelectionFactors,

	@Schema(description = "선호하는 기업 문화 리스트")
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
