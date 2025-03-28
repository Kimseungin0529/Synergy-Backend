package com.synergy.backend.domain.session.dto.questionDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record QuestionReqDto(

        @NotBlank(message = "질문은 최소 10자로 작성해야합니다.")
        @Schema(description = "세션에 등록할 질문 내용", example = "이 기술을 실무에 적용할 때 가장 주의해야 할 점은 무엇인가요?")
        @Size(min = 10, max = 300, message = "질문은 최소 10자 이상, 최대 300자 이하여야 합니다.")
        String content
) {
}
