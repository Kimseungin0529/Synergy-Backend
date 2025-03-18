package com.synergy.backend.domain.session.dto.sessionDto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SessionReqDto(
        @NotNull
        String title,
        @NotNull
        String speaker,
        @NotNull
        String speakerPosition,
        @NotNull
        String progressDate,
        @NotNull
        String startTime,
        @NotNull
        String endTime,
        @NotNull
        @Size(max = 100)
        String description,
        Integer maximum,
        String domainAddress) {


}
