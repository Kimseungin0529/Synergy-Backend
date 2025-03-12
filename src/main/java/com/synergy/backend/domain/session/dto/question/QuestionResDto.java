package com.synergy.backend.domain.session.dto.question;

import com.synergy.backend.domain.session.entity.AttendeeSession;
import lombok.Builder;

public record QuestionResDto(
        String name,
        String content
) {

    public static QuestionResDto from(AttendeeSession attendeeSession) {
        return new QuestionResDto(attendeeSession.getAttendee().getName(), attendeeSession.getQuestion());
    }
}
