package com.synergy.backend.domain.booth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BoothRequestDto(
        @NotBlank(message = "기업명은 필수입니다.")
        @Schema(description = "부스를 운영하는 기업의 이름", example = "CodeSphere")
        String companyName,

        @NotBlank(message = "기업 유형은 필수입니다.")
        @Schema(description = "기업의 유형", example = "클라우드")
        String companyType,

        @NotNull(message = "부스 진행 날짜는 필수입니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @Schema(description = "부스가 진행되는 날짜", example = "2025-05-13")
        LocalDate progressDate,

        @NotBlank(message = "부스 위치는 필수입니다.")
        @Schema(description = "부스가 위치한 장소", example = "C HALL")
        String boothLocation,

        @NotBlank(message = "부스 번호는 필수입니다.")
        @Schema(description = "부스 번호", example = "234C")
        String boothNumber,

        @NotBlank(message = "부스 설명은 필수입니다.")
        @Schema(description = "부스에서 제공하는 서비스 내용 및 채용 정보", example = "클라우드서비스: 글로벌 IT 기업 CodeSphere에서 React 기반 프론트엔드 엔지니어와 클라우드 기반 백엔드 엔지니어를 채용합니다. TypeScript, Node.js, Kubernetes 경험자를 환영합니다.")
        String boothDescription
) {
}
