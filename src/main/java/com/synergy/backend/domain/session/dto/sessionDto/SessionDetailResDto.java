package com.synergy.backend.domain.session.dto.sessionDto;

import com.synergy.backend.domain.session.dto.questionDto.QuestionResDto;
import com.synergy.backend.domain.session.entity.Session;

import java.time.LocalDateTime;
import java.util.List;

public record SessionDetailResDto(
        Long sessionId,
        String title,
        String speaker,
        String speakerPosition,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String description,
        String imageUrl,
        List<QuestionResDto> questionResDto,
        Boolean isQRVerify
) {
    public static SessionDetailResDto from(Session session, List<QuestionResDto> resDtos, Boolean isQRVerify) {
        return new SessionDetailResDto(session.getId(), session.getTitle(), session.getSpeaker(), session.getSpeakerPosition(),
                session.getStartTime(), session.getEndTime(), session.getDescription(), session.getImageUrl(), resDtos, isQRVerify);
    }

}
