package com.synergy.backend.domain.member.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.synergy.backend.domain.member.api.dto.response.AttendeePointRankingResponseDto;

public interface AttendeeRedisRankingRepository {
	void saveRanking(Long conferenceId, AttendeePointRankingResponseDto attendeePointRankingResponseDto);

	List<AttendeePointRankingResponseDto> getRanking(Long conferenceId, long start, long end);

	Page<AttendeePointRankingResponseDto> getRankingPage(Long conferenceId, Pageable pageable);

	void deleteRanking(Long conferenceId);

	Long getTotalCount(Long conferenceId);
}
