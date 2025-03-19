package com.synergy.backend.domain.session.dto.sessionDto;

import com.synergy.backend.domain.session.dto.questionDto.QuestionResDto;
import com.synergy.backend.domain.session.entity.Session;

import java.time.LocalDateTime;
import java.util.List;

public record SessionDetailResDto(
        Long sessionId,
        String title,
        String speaker,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String description,
        List<QuestionResDto> questionResDto
) {
    public static SessionDetailResDto from(Session session, List<QuestionResDto> resDtos) {
        return new SessionDetailResDto(session.getId(), session.getTitle(), session.getSpeaker(),
                session.getStartTime(), session.getEndTime(), session.getDescription(), resDtos);
    }

    public static SessionDetailResDto withQRCodefrom(Session session, List<QuestionResDto> resDtos) {
        return new SessionDetailResDto(session.getId(), session.getTitle(), session.getSpeaker(),
                session.getStartTime(), session.getEndTime(), session.getDescription(), resDtos);
    }
}
