package com.synergy.backend.domain.member.api.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AttendeeSimpleResponseDto {
    private String name;
    private String profileUrl;
    private String occupation;
    private String techStacks;
    private String experienceLevel;


    @QueryProjection
    public AttendeeSimpleResponseDto(String name, String profileUrl, String occupation, String experienceLevel, String techStacks) {
        this.name = name;
        this.profileUrl = profileUrl;
        this.occupation = occupation;
        this.experienceLevel = experienceLevel;
        this.techStacks = techStacks;
    }
}
