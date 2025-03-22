package com.synergy.backend.domain.member.api.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class AttendeeListResponse {
    private final List<AttendeeSimpleResponse> list;

    private AttendeeListResponse(List<AttendeeSimpleResponse> list) {
        this.list = list;
    }

    public static AttendeeListResponse from(List<AttendeeSimpleResponse> list) {
        return new AttendeeListResponse(list);
    }
}
