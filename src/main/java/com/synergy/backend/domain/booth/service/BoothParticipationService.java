package com.synergy.backend.domain.booth.service;

import com.synergy.backend.domain.booth.dto.boothParticipateDto.BoothParticipateRateResDto;
import com.synergy.backend.domain.booth.dto.boothParticipateDto.BoothParticipationInterestedResponseDto;
import com.synergy.backend.domain.booth.dto.BoothResponseDto;

import java.util.List;

public interface BoothParticipationService {
    BoothResponseDto participateInBooth(String identifier, Long boothId, String qrCode);

    BoothParticipateRateResDto boothParticipateRate(String identifier, Long conferenceId);

    List<BoothParticipationInterestedResponseDto> getParticipationCountByInterest(Long boothId);
}
