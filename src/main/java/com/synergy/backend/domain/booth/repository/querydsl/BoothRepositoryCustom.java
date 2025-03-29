package com.synergy.backend.domain.booth.repository.querydsl;

import com.synergy.backend.domain.booth.dto.boothParticipateDto.BoothParticipationInterestedResponseDto;

import java.util.List;

public interface BoothRepositoryCustom {

    BoothPar

    List<BoothParticipationInterestedResponseDto> searchBoothParticipation(Long conferenceId);
}
