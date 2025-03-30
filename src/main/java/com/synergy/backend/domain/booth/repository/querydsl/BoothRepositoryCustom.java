package com.synergy.backend.domain.booth.repository.querydsl;

import com.synergy.backend.domain.booth.dto.boothParticipateDto.BoothParticipateRateResDto;
import com.synergy.backend.domain.booth.dto.boothParticipateDto.BoothParticipationInterestedResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface BoothRepositoryCustom {

    BoothParticipateRateResDto searchBoothRank(Long conferenceId, LocalDate currentDate);

    List<BoothParticipationInterestedResponseDto> searchBoothParticipation(Long conferenceId);
}
