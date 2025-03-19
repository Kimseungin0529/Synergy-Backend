package com.synergy.backend.domain.conference.dto.requset;

import jakarta.validation.constraints.Future;

import java.time.LocalDateTime;

public record ConferenceUpdateRequest(
    String name,

    @Future(message = "시작 날짜는 미래여야 합니다.")
    LocalDateTime startDate,

    @Future(message = "종료 날짜는 미래여야 합니다.")
    LocalDateTime endDate,

    String location,
    String organizer,
    String type
) {
}

