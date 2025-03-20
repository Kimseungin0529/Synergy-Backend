package com.synergy.backend.domain.member.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.synergy.backend.domain.member.api.dto.resposne.AttendeeLevelRankingResponseDto;
import com.synergy.backend.domain.member.api.dto.resposne.AttendeePointRankingResponseDto;
import com.synergy.backend.domain.member.entity.details.MembershipLevelType;

public interface AdminService {
	Page<AttendeeLevelRankingResponseDto> getAttendeeLevelRankings(MembershipLevelType grade, Pageable pageable);

	Page<AttendeePointRankingResponseDto> getAttendeePointRankings(Pageable pageable);
}
