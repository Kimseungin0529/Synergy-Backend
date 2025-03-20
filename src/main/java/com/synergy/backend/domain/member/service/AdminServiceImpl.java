package com.synergy.backend.domain.member.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.synergy.backend.domain.member.api.dto.resposne.AttendeeLevelRankingResponseDto;
import com.synergy.backend.domain.member.api.dto.resposne.AttendeePointRankingResponseDto;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.details.MembershipLevelType;
import com.synergy.backend.domain.member.repository.AttendeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

	private final AttendeeRepository attendeeRepository;

	@Transactional(readOnly = true)
	@Override
	public Page<AttendeeLevelRankingResponseDto> getAttendeeLevelRankings(MembershipLevelType membershipLevel,
		Pageable pageable) {

		if (membershipLevel != null) {
			return attendeeRepository
				.findByMembershipLevelTypeOrderByTotalPointsDesc(membershipLevel, pageable)
				.map(AttendeeLevelRankingResponseDto::from);
		}

		return getSortedAttendeePage(pageable)
			.map(AttendeeLevelRankingResponseDto::from);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<AttendeePointRankingResponseDto> getAttendeePointRankings(Pageable pageable) {
		return getSortedAttendeePage(pageable).map(AttendeePointRankingResponseDto::from);
	}

	private Page<Attendee> getSortedAttendeePage(Pageable pageable) {
		Pageable sortedPageable = PageRequest.of(
			pageable.getPageNumber(),
			pageable.getPageSize(),
			Sort.by(Sort.Direction.DESC, "totalPoints")
		);
		return attendeeRepository.findAllByOrderByTotalPointsDesc(sortedPageable);
	}
}
