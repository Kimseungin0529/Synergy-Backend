package com.synergy.backend.domain.member.api.dto.request;

import java.util.Set;

import jakarta.validation.constraints.Size;

public record InterestRequestDto(
	@Size(max = 3, message = "관심분야는 최대 3개까지 선택 가능합니다.")
	Set<Integer> interestCodes

) {
}
