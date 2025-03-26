package com.synergy.backend.domain.conference.dto.requset;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record ConferenceUpdateRequest(
        String name,
        String organizer,

        @Future(message = "시작 날짜는 미래여야 합니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate startDate,

        @JsonFormat(pattern = "HH:mm")
        LocalTime startTime,

        @Future(message = "종료 날짜는 미래여야 합니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate endDate,

        @JsonFormat(pattern = "HH:mm")
        LocalTime endTime,

        String location,
        String position,
        String type


) {
}

