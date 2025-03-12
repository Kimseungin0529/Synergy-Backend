package com.synergy.backend.domain.session.dto.question;

public record QuestionParticipateResDto(
        Long sessionId,
        String title,
        Double participationRate
) {
}
