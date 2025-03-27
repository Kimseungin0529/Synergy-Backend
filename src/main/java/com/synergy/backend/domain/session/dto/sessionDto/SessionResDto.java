package com.synergy.backend.domain.session.dto.sessionDto;

import com.synergy.backend.domain.session.entity.Session;

import java.time.LocalDateTime;

public record SessionResDto(
        Long id,
        String title,
        String speaker,
        String speakerPosition,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String imageUrl
) {
    public static SessionResDto from(Session session){
        return new SessionResDto(session.getId(), session.getTitle(), session.getSpeaker(), session.getSpeakerPosition(),
                session.getStartTime(), session.getEndTime(), session.getImageUrl());
    }

}
