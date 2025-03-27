package com.synergy.backend.domain.member.api.dto.request;

import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record JobInfoRequestDto(
	@NotNull(message = "관심 분야는 필수입니다.")
	@Size(max = 3, message = "관심분야는 최대 3개까지 선택 가능합니다.")
	@Schema(
		description = "관심 분야 코드 목록 (최대 3개 선택 가능)",
		example = "[101, 102, 103]"
	)
	Set<Integer> interestCodes,

	@NotNull(message = "직군 코드는 필수입니다.")
	@Schema(
		description = "직군 코드 (예: 개발, 디자인, 기획 등)",
		example = "1"
	)
	Integer jobGroupCode,

	@NotNull(message = "직무 코드는 필수입니다.")
	@Schema(
		description = "직무 코드 (예: 프론트엔드, 백엔드, UI 디자이너 등)",
		example = "101"
	)
	Integer jobPositionCode,

	@NotNull(message = "채용 관심 여부는 필수입니다.")
	@Schema(
		description = "채용 제안에 관심 있음 여부 (true 또는 false)",
		example = "true"
	)
	Boolean hiringInterested
) {
}
