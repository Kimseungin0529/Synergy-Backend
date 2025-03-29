package com.synergy.backend.domain.conference.dto.requset;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record ConferenceUpdateRequest(

        @NotBlank(message = "컨퍼런스명은 필수입니다.")
        @Schema(description = "수정할 컨퍼런스 제목", example = "F’LINK 2025")
        String name,

        @NotBlank(message = "주최자명은 필수입니다.")
        @Schema(description = "수정할 주최자 이름 또는 기관", example = "FlowLink")
        String host,

        @NotNull(message = "시작 날짜는 필수입니다.")
        @Future(message = "시작 날짜는 미래여야 합니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @Schema(description = "수정할 시작 날짜", example = "2025-06-01")
        LocalDate startDate,

        @NotNull(message = "시작 시간은 필수입니다.")
        @JsonFormat(pattern = "HH:mm")
        @Schema(description = "수정할 시작 시간", example = "10:00")
        LocalTime startTime,

        @NotNull(message = "종료 날짜는 필수입니다.")
        @Future(message = "종료 날짜는 미래여야 합니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @Schema(description = "수정할 종료 날짜", example = "2025-06-01")
        LocalDate endDate,

        @NotNull(message = "종료 시간은 필수입니다.")
        @JsonFormat(pattern = "HH:mm")
        @Schema(description = "수정할 종료 시간", example = "18:00")
        LocalTime endTime,

        @NotBlank(message = "컨퍼런스 위치는 필수입니다.")
        @Schema(description = "수정할 컨퍼런스 위치", example = "로비 A")
        String location,

        @NotBlank(message = "장소 정보는 필수입니다.")
        @Schema(description = "수정할 장소 정보", example = "그랜드볼룸")
        String place,

        @NotBlank(message = "컨퍼런스 유형은 필수입니다.")
        @Schema(description = "수정할 컨퍼런스 유형", example = "IT")
        String conferenceType

) {}
