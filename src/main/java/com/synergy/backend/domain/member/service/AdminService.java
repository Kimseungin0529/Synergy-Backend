package com.synergy.backend.domain.member.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.synergy.backend.domain.member.api.dto.response.AttendeeLevelRankingResponseDto;
import com.synergy.backend.domain.member.api.dto.response.AttendeePointRankingResponseDto;
import com.synergy.backend.domain.member.entity.details.MembershipLevelType;

public interface AdminService {
	Page<AttendeeLevelRankingResponseDto> getAttendeeLevelRankings(Long conferenceId, MembershipLevelType grade, Pageable pageable);

	Page<AttendeePointRankingResponseDto> getAttendeePointRankings(Long conferenceId, Pageable pageable);
}
