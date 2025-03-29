package com.synergy.backend.domain.booth.service;

import com.synergy.backend.domain.booth.dto.BoothParticipationResponseDto;
import com.synergy.backend.domain.booth.dto.BoothResponseDto;

import java.util.List;

public interface BoothParticipationService {
    BoothResponseDto participateInBooth(String identifier, Long boothId, String qrCode);

    List<BoothParticipationResponseDto> getParticipationCountByInterest(Long boothId);
}
