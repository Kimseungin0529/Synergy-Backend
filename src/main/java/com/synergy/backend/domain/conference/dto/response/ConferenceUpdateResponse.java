package com.synergy.backend.domain.conference.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.conference.entity.TimePeriod;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record ConferenceUpdateResponse(
        String name,
        String organizer,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate startDate,

        @JsonFormat(pattern = "HH:mm")
        LocalTime startTime,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate endDate,

        @JsonFormat(pattern = "HH:mm")
        LocalTime endTime,

        String location,
        String position,
        String type
) {
    public static ConferenceUpdateResponse from(Conference conference) {
        TimePeriod period = conference.getPeriod();
        return new ConferenceUpdateResponse(conference.getName(),
                conference.getOrganizer(),
                period.getStartDate(),
                period.getStartTime(),
                period.getEndDate(),
                period.getEndTime(),
                conference.getLocation(),
                conference.getPosition(),
                conference.getType());
    }


}
