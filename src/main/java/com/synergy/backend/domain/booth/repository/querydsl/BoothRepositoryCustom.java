package com.synergy.backend.domain.booth.repository.querydsl;

import com.synergy.backend.domain.booth.dto.boothParticipateDto.BoothParticipationResponseDto;

import java.util.List;

public interface BoothRepositoryCustom {

    List<BoothParticipationResponseDto> searchBoothParticipation(Long conferenceId);
}
