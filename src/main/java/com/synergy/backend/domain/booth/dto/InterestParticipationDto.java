package com.synergy.backend.domain.booth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InterestParticipationDto {
    private String interest;
    private Long count;
}
