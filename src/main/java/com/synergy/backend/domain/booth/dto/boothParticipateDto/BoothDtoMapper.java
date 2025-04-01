package com.synergy.backend.domain.booth.dto.boothParticipateDto;

import com.synergy.backend.domain.booth.dto.boothParticipateDto.collection.BoothParticipateInterestedTechDtoList;

public class BoothDtoMapper {

    public static BoothParticipationInterestedResponseDto withProcessedTechs(
            BoothParticipationInterestedResponseDto original,
            BoothParticipateInterestedTechDtoList processed
    ) {
        return new BoothParticipationInterestedResponseDto(
                original.getBoothId(),
                original.getCompanyName(),
                original.getCompanyType(),
                original.getBoothLocation(),
                original.getBoothNumber(),
                original.getBoothDescription(),
                original.getProgressDate(),
                original.getQrCode(),
                processed.getValues()
        );
    }
}
