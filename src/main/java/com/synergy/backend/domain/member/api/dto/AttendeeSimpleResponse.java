package com.synergy.backend.domain.member.api.dto;

import com.synergy.backend.domain.member.entity.Attendee;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AttendeeSimpleResponse {
    private String name;
    private String profileUrl;
    private String occupation;
    private String experienceLevel;
    private String techStacks;

    @Builder
    public AttendeeSimpleResponse(Attendee attendee) {

    }
}
