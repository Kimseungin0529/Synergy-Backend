package com.synergy.backend.domain.conference.dto.response;

public record ConferenceAttendeeInfoResDto(
        Long sessionAttendeeCount,
        Long boothAttendeeCount,
        Long conferenceAttendeeCount,
        Long serviceAttendeeCount
) {
    public static ConferenceAttendeeInfoResDto from(Long sessionAttendeeCount, Long boothAttendeeCount,
                                                    Long conferenceAttendeeCount, Long serviceAttendeeCount) {
        return new ConferenceAttendeeInfoResDto(sessionAttendeeCount, boothAttendeeCount,
                conferenceAttendeeCount, serviceAttendeeCount);
    }
}
