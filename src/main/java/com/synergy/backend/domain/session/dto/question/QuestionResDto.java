package com.synergy.backend.domain.session.dto.question;

import com.synergy.backend.domain.session.entity.AttendeeSession;
import lombok.Builder;

public record QuestionResDto(
        Long id,
        String name,
        String content
) {

}
