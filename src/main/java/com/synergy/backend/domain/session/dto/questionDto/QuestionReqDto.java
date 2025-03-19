package com.synergy.backend.domain.session.dto.questionDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record QuestionReqDto(

        @NotBlank(message = "질문은 최소 10자로 작성해야합니다.")
        @Size(min = 10, max = 300)
        String content
) {
}
