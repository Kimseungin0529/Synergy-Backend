package com.synergy.backend.domain.conference.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.conference.entity.TimePeriod;

import java.time.LocalDateTime;

public record ConferenceUpdateResponse(
        String name,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime startDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime endDate,
        String location,
        String organizer,
        String type
) {
    public static ConferenceUpdateResponse from(Conference conference) {
        TimePeriod period = conference.getPeriod();
        return new ConferenceUpdateResponse(conference.getName(),
                period.getStartDateTime(),
                period.getEndDateTime(),
                conference.getLocation(),
                conference.getOrganizer(),
                conference.getType());
    }

}
