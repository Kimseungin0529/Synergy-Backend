package com.synergy.backend.domain.member.api.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class AttendeeListResponse {
    private final List<AttendeeSimpleResponseDto> list;

    private AttendeeListResponse(List<AttendeeSimpleResponseDto> list) {
        this.list = list;
    }

    public static AttendeeListResponse from(List<AttendeeSimpleResponseDto> list) {
        return new AttendeeListResponse(list);
    }
}
