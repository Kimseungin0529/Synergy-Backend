package com.synergy.backend.domain.booth.dto.boothParticipateDto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class BoothParticipateInterestedTechDto {
    private Long boothId;
    private String interestedTech;
    private long count;

    @QueryProjection
    public BoothParticipateInterestedTechDto(Long boothId, long count, String interestedTech) {
        this.boothId = boothId;
        this.count = count;
        this.interestedTech = interestedTech;
    }
}
