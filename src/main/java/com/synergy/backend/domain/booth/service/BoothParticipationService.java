package com.synergy.backend.domain.booth.service;

import com.synergy.backend.domain.booth.dto.BoothParticipationResponseDto;

import java.util.List;

public interface BoothParticipationService {
    void participateInBooth(Long attendeeId, Long boothId);

    List<BoothParticipationResponseDto> getParticipationCountByInterest(Long boothId);
}
