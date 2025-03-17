package com.synergy.backend.domain.session.dto;


public record SessionReqDto(
        String title,
        String speaker,
        String speakerPosition,
        String progressDate,
        String startTime,
        String endTime,
        String description,
        String domainAddress) {


}
