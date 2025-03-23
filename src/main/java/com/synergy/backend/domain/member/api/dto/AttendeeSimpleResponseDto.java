package com.synergy.backend.domain.member.api.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.synergy.backend.domain.member.entity.details.ExperienceLevelType;
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
    private boolean isLiked;


    @QueryProjection
    public AttendeeSimpleResponseDto(String name, String profileUrl, String occupation, ExperienceLevelType experienceLevel, String techStacks, boolean isLiked) {
        this.name = name;
        this.profileUrl = profileUrl;
        this.occupation = occupation;
        this.experienceLevel = experienceLevel != null ? experienceLevel.getDescription() : null;
        this.techStacks = techStacks;
        this.isLiked = isLiked;
    }
}
