package com.synergy.backend.domain.session.dto.sessionDto;

import com.synergy.backend.domain.session.entity.Session;

import java.time.LocalDateTime;

public record SessionResDto(
        Long id,
        String title,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
    public static SessionResDto from(Session session){
        return new SessionResDto(session.getId(), session.getTitle(), session.getStartTime(), session.getEndTime());
    }

}
