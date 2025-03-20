package com.synergy.backend.domain.session.dto.sessionDto;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SessionReqDto(
        @NotNull
        String title,
        @NotNull
        String speaker,
        @NotNull
        String speakerPosition,
        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate progressDate,
        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime startTime,
        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime endTime,
        @NotNull
        @Size(max = 100)
        String description,
        Integer maximum,
        String domainAddress) {


}
