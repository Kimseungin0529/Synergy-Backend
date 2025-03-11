package com.synergy.backend.domain.conference.dto.response;

import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.conference.entity.TimePeriod;

import java.time.LocalDateTime;

public record ConferenceUpdateResponse(
        String name,
        LocalDateTime startTime,
        LocalDateTime endTime,
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
