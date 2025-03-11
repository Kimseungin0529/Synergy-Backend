package com.synergy.backend.domain.conference.dto.requset;


import jakarta.persistence.Column;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record ConferenceCreateRequest(
        @NotBlank(message = "컨퍼런스명은 필수입니다. 공백 이하는 불가능합니다.")
        String name,
        @Future(message = "시작 날짜는 미래여야 합니다.")
        LocalDateTime startDate,
        @Future(message = "종료 날짜는 미래여야 합니다.")
        LocalDateTime endDate,
        @NotBlank(message = "컨퍼런스 위치 정보는 필수입니다. 공백 이하는 불가능합니다.")
        String location,
        @NotBlank(message = "주최자명은 필수입니다. 공백 이하는 불가능합니다.")
        String organizer,
        @NotBlank(message = "컨퍼런스 유형은 필수입니다. 공백 이하는 불가능합니다.")
        String type
) {
}

