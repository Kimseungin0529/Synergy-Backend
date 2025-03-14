package com.synergy.backend.domain.member.api.dto.request;

import java.util.Set;

public record InterestRequestDto(Set<Long> interestIds) {
}
