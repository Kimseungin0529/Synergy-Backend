package com.synergy.backend.domain.session.dto.sessionDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SessionReqDto(

        @NotBlank(message = "세션 제목은 필수입니다.")
        @Schema(description = "세션 제목", example = "디지털 시대의 리더십과 팀 빌딩")
        String title,

        @NotBlank(message = "연사 이름은 필수입니다.")
        @Schema(description = "연사 이름", example = "홍길동")
        String speaker,

        @NotBlank(message = "연사 직책은 필수입니다.")
        @Schema(description = "연사 직책", example = "FlowLink HR 팀 총괄")
        String speakerPosition,

        @NotNull(message = "세션 진행 날짜는 필수입니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @Schema(description = "세션이 진행되는 날짜", example = "2025-06-10")
        LocalDate progressDate,

        @NotNull(message = "세션 시작 시간은 필수입니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
        @Schema(description = "세션 시작 시간", example = "2025-06-10T14:00")
        LocalDateTime startTime,

        @NotNull(message = "세션 종료 시간은 필수입니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
        @Schema(description = "세션 종료 시간", example = "2025-06-10T15:00")
        LocalDateTime endTime,

        @NotBlank(message = "세션 설명은 필수입니다.")
        @Size(max = 3000, message = "세션 설명은 3000자 이내여야 합니다.")
        @Schema(description = "세션 소개 또는 주제에 대한 설명", example = "디지털 시대의 리더십과 팀 빌딩', '빠르게 변화하는 IT 산업에서 최신 기술 동향을 파악하는 것은 기업의 경쟁력을 높이고 미래를 준비하는 데 필수적입니다. 기업의 CTO가 AI, 클라우드, Web3 등 주요 기술 트렌드와 산업 변화를 분석하고, 기업이 기술 혁신을 어떻게 주도할 수 있는지에 대한 전략과 인사이트를 제공합니다.")
        String description,

        @NotNull(message = "최대 인원 수는 필수입니다.")
        @Schema(description = "세션 최대 참석 인원 수", example = "100")
        Integer maximum

) {}
