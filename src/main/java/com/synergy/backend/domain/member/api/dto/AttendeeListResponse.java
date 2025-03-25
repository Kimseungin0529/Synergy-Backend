package com.synergy.backend.domain.member.api.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

import com.synergy.backend.domain.member.api.dto.resposne.AttendeeSimpleResponseDto;

@Getter
public class AttendeeListResponse {
    private final int currentPageNumber;
    private final int totalPages;
    private final long totalElements;
    private final int pageSize;
    private final List<AttendeeSimpleResponseDto> list;

    private AttendeeListResponse(int currentPageNumber, int totalPages, long totalElementsList, int pageSize, List<AttendeeSimpleResponseDto> list) {
        this.currentPageNumber = currentPageNumber;
        this.totalPages = totalPages;
        this.totalElements = totalElementsList;
        this.pageSize = pageSize;
        this.list = list;
    }

    public static AttendeeListResponse from(Page<AttendeeSimpleResponseDto> page) {
        return new AttendeeListResponse(
                page.getNumber(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getSize(),
                page.getContent()
        );

    }
}
