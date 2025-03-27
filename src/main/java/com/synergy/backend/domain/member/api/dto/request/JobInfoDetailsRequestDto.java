package com.synergy.backend.domain.member.api.dto.request;

import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record JobInfoDetailsRequestDto(
	@NotNull(message = "희망 직군은 필수입니다.")
	@Schema(description = "희망 직군 코드", example = "1")
	Integer desiredJobGroupCode,

	@NotNull(message = "희망 직무는 필수입니다.")
	@Schema(description = "희망 직무 코드", example = "101")
	Integer desiredJobPositionCode, // 희망 직무 (코드값)

	@NotNull(message = "학력 정보는 필수입니다.")
	@Schema(description = "학력 코드", example = "3")
	Integer educationLevelCode, // 학력 (코드값)

	@NotNull(message = "연령대는 필수입니다.")
	@Schema(description = "연령대 코드", example = "20")
	Integer ageGroupCode, // 연령대 (코드값)

	@NotBlank(message = "보유 기술 값은 비어 있을 수 없습니다.")
	@Schema(description = "보유 기술 목록 (쉼표로 구분된 문자열)", example = "Java, Spring, MySQL")
	String techStacks, // 보유 기술 (스트링)

	@NotNull(message = "경력은 필수입니다.")
	@Schema(description = "경력 코드", example = "2")
	Integer experienceLevelCode, // 경력 (코드값)

	@NotNull(message = "희망 근무 지역은 필수입니다.")
	@Schema(description = "희망 근무 지역 코드 목록", example = "[11, 22]")
	Set<Integer> desiredWorkRegionCodes, // 희망 근무 지역 (코드값 리스트)

	@Size(max = 400, message = "자기소개서는 400자 이내여야 합니다.")
	@NotNull(message = "자기소개서는 필수입니다.")
	@Schema(description = "자기소개서 (400자 이내)", example = "끊임없이 배우고 성장하는 개발자입니다.")
	String selfIntroduction, // 자기소개서

	@Schema(description = "기타 경험 및 추가 정보", example = "스타트업 인턴 경험, 외부 해커톤 참가")
	String additionalInfo, // 경험 및 기타 정보

	@Size(max = 3, message = "직장 선택 요소는 최대 3개까지 선택 가능합니다.")
	@Schema(description = "직장 선택 요소 코드 목록 (최대 3개)", example = "[1, 2]")
	Set<Integer> workplaceSelectionFactorCodes, // 직장 선택 요소 (코드값 리스트)

	@Size(max = 3, message = "선호하는 기업 문화는 최대 3개까지 선택 가능합니다.")
	@Schema(description = "선호하는 기업 문화 코드 목록 (최대 3개)", example = "[1, 2]")
	Set<Integer> preferredCorporateCultureCodes, // 선호하는 기업 문화 (코드값 리스트)

	@Size(max = 3, message = "컨퍼런스 참여 목적은 최대 3개까지 선택 가능합니다.")
	@Schema(description = "컨퍼런스 참여 목적 코드 목록 (최대 3개)", example = "[1, 2]")
	Set<Integer> conferencePurposeCodes // 컨퍼런스 참여 목적 (코드값 리스트)
) {
}
