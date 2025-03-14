package com.synergy.backend.domain.member.api.dto.resposne;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.synergy.backend.domain.interest.entity.Interest;

public record InterestResponseDto(List<String> interests) {
	public static InterestResponseDto from(Set<Interest> interests) {
		List<String> names = interests.stream()
			.map(Interest::getName)
			.collect(Collectors.toList());

		return new InterestResponseDto(names);
	}
}
