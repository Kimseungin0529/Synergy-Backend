package com.synergy.backend.domain.member.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.synergy.backend.domain.member.api.dto.response.AttendeeLevelRankingResponseDto;
import com.synergy.backend.domain.member.api.dto.response.AttendeePointRankingResponseDto;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.details.MembershipLevelType;
import com.synergy.backend.domain.member.repository.AttendeeRedisRankingRepository;
import com.synergy.backend.domain.member.repository.AttendeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

	private final AttendeeRepository attendeeRepository;
	private final AttendeeRedisRankingRepository attendeeRedisRankingRepository;

	@Transactional(readOnly = true)
	@Override
	public Page<AttendeeLevelRankingResponseDto> getAttendeeLevelRankings(Long conferenceId,
		MembershipLevelType membershipLevel,
		Pageable pageable) {

		if (membershipLevel != null) {
			return attendeeRepository
				.findByMembershipLevelTypeOrderByTotalPointsDesc(membershipLevel, pageable)
				.map(AttendeeLevelRankingResponseDto::from);
		}

		return getSortedAttendeePage(conferenceId, pageable)
			.map(AttendeeLevelRankingResponseDto::from);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<AttendeePointRankingResponseDto> getAttendeePointRankings(Long conferenceId, Pageable pageable) {
		Page<AttendeePointRankingResponseDto> page = attendeeRedisRankingRepository.getRankingPage(conferenceId,
			pageable);

		if (page.getTotalElements() == 0) {
			// 캐시 미스 시 DB 조회 (1000명)
			Page<AttendeePointRankingResponseDto> allRankedAttendees = attendeeRepository.findTopAttendeeRankingsDtoByConferenceId(
				conferenceId, PageRequest.of(0, 1000));

			// Redis에 저장
			for (AttendeePointRankingResponseDto dto : allRankedAttendees) {
				attendeeRedisRankingRepository.saveRanking(conferenceId, dto);
			}

			// Redis에서 다시 조회
			page = attendeeRedisRankingRepository.getRankingPage(conferenceId, pageable);
		}

		return page;
	}

	private Page<Attendee> getSortedAttendeePage(Long conferenceId, Pageable pageable) {
		Pageable sortedPageable = PageRequest.of(
			pageable.getPageNumber(),
			pageable.getPageSize(),
			Sort.by(Sort.Direction.DESC, "totalPoints")
		);
		return attendeeRepository.findByConferenceIdOrderByTotalPointsDesc(conferenceId, sortedPageable);
	}
}
