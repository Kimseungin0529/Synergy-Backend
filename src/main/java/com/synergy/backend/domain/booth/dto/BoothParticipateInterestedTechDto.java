package com.synergy.backend.domain.booth.dto;

import com.querydsl.core.annotations.QueryProjection;

public class BoothParticipateInterestedTechDto {
    private String interestedTech;
    private long count;

    @QueryProjection
    public BoothParticipateInterestedTechDto(long count, String interestedTech) {
        this.count = count;
        this.interestedTech = interestedTech;
    }
}
