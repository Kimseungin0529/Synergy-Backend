package com.synergy.backend.domain.conference.dto.requset;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record ConferenceCreateRequest(
        @NotBlank(message = "컨퍼런스명은 필수입니다. 공백 이하는 불가능합니다.")
        @Schema(description = "컨퍼런스 제목", example = "F’LINK 2025")
        String name,

        @NotBlank(message = "주최자명은 필수입니다. 공백 이하는 불가능합니다.")
        @Schema(description= "주최자 이름", example = "FlowLink")
        String host,

        @NotNull(message = "시작 날짜는 필수입니다.")
        @Future(message = "시작 날짜는 미래여야 합니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @Schema(description= "컨퍼런스 시작 날짜", example = "2025-06-01")
        LocalDate startDate,

        @NotNull(message = "시작 시간은 필수입니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        @Schema(description = "컨퍼런스 시작 시간", example = "10:00")
        LocalTime startTime,

        @NotNull(message = "종료 날짜는 필수입니다.")
        @Future(message = "종료 날짜는 미래여야 합니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @Schema(description = "컨퍼런스 종료 날짜", example = "2025-06-01")
        LocalDate endDate,

        @NotNull(message = "종료 시간은 필수입니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        @Schema(description = "컨퍼런스 종료 시간", example = "18:00")
        LocalTime endTime,

        @NotBlank(message = "컨퍼런스 위치 정보는 필수입니다. 공백 이하는 불가능합니다.")
        @Schema(description = "컨퍼런스 위치", example = "로비 A")
        String location,

        @NotBlank(message = "컨퍼런스 장소 정보는 필수입니다. 공백 이하는 불가능합니다.")
        @Schema(description = "컨퍼런스 장소", example = "그랜드볼룸")
        String place,

        @NotBlank(message = "컨퍼런스 유형은 필수입니다. 공백 이하는 불가능합니다.")
        @Schema(description = "컨퍼런스 유형", example = "IT")
        String conferenceType
) {
}

