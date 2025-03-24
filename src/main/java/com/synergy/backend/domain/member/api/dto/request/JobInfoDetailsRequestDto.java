package com.synergy.backend.domain.member.api.dto.request;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record JobInfoDetailsRequestDto(
	@NotNull
	Integer desiredJobGroupCode, // 희망 직군 (코드값)

	@NotNull
	Integer desiredJobPositionCode, // 희망 직무 (코드값)

	@NotNull
	Integer educationLevelCode, // 학력 (코드값)

	@NotNull
	Integer ageGroupCode, // 연령대 (코드값)

	@NotBlank(message = "보유 기술 값은 비어 있을 수 없습니다.")
	String techStacks, // 보유 기술 (스트링 리스트)

	@NotNull
	Integer experienceLevelCode, // 경력 (코드값)

	@NotNull
	Set<Integer> preferredRegionCodes, // 희망 근무 지역 (코드값 리스트)

	String selfIntroduction, // 자기소개서
	String profileImageUrl, // 증명사진 (파일 업로드 시 URL 저장)
	String additionalInfo, // 경험 및 기타 정보

	@Size(max = 3, message = "직장 선택 요소는 최대 3개까지 선택 가능합니다.")
	Set<Integer> workplaceSelectionFactorCodes, // 직장 선택 요소 (코드값 리스트)

	@Size(max = 3, message = "선호하는 기업 문화는 최대 3개까지 선택 가능합니다.")
	Set<Integer> preferredCorporateCultureCodes, // 선호하는 기업 문화 (코드값 리스트)

	@Size(max = 3, message = "컨퍼런스 참여 목적은 최대 3개까지 선택 가능합니다.")
	Set<Integer> conferencePurposeCodes // 컨퍼런스 참여 목적 (코드값 리스트)
) {
}
