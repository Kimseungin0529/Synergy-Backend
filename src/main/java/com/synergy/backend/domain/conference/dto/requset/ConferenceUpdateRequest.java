package com.synergy.backend.domain.conference.dto.requset;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;

import java.time.LocalDateTime;

public record ConferenceUpdateRequest(
    String name,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Future(message = "시작 날짜는 미래여야 합니다.")
    LocalDateTime startDate,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Future(message = "종료 날짜는 미래여야 합니다.")
    LocalDateTime endDate,

    String location,
    String organizer,
    String type
) {
}

