package com.synergy.backend.domain.member.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.synergy.backend.domain.member.api.dto.resposne.AttendeeLevelRankingResponseDto;
import com.synergy.backend.domain.member.api.dto.resposne.AttendeePointRankingResponseDto;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.details.MembershipLevelType;
import com.synergy.backend.domain.member.repository.AttendeeRepository;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@Slf4j
class AdminServiceImplTest {

	@Mock
	AttendeeRepository attendeeRepository;

	@InjectMocks
	AdminServiceImpl adminService;

	private Attendee attendee1;
	private Attendee attendee2;
	private PageRequest pageable;

	@BeforeEach
	void setUp() {
		attendee1 = Attendee.of("user1@email.com", "pass", "user1", "010110");
		attendee1.addTotalPoints(1000);
		assertThat(attendee1.getMembershipLevelType()).isEqualTo(MembershipLevelType.GOLD);

		attendee2 = Attendee.of("user2@email.com", "pass", "user2", "010110");
		attendee2.addTotalPoints(50);
		assertThat(attendee2.getMembershipLevelType()).isEqualTo(MembershipLevelType.DEFAULT);

		pageable = PageRequest.of(0, 10);
	}

	@DisplayName("등급이 GOLD인 참가자 랭킹을 조회한다.")
	@Test
	void getAttendeeLevelRankingsWithMembershipLevel() {
		// given
		List<Attendee> attendees = List.of(attendee1);
		Page<Attendee> attendeePage = new PageImpl<>(attendees, pageable, attendees.size());

		when(attendeeRepository.findByMembershipLevelTypeOrderByTotalPointsDesc(
			eq(MembershipLevelType.GOLD), any(Pageable.class)
		)).thenReturn(attendeePage);

		// when
		Page<AttendeeLevelRankingResponseDto> response = adminService.getAttendeeLevelRankings(
			MembershipLevelType.GOLD, pageable);

		// then
		assertThat(response).hasSize(1);
		assertThat(response.getContent().get(0).attendeeName()).isEqualTo("user1");
	}

	@DisplayName("등급별 참가자 랭킹을 필터 없이 조회한다.")
	@Test
	void getAttendeeLevelRankings() {
		// given
		List<Attendee> attendees = List.of(attendee1, attendee2);
		Page<Attendee> attendeePage = new PageImpl<>(attendees, pageable, attendees.size());

		when(attendeeRepository.findAllByOrderByTotalPointsDesc(any(Pageable.class))).thenReturn(attendeePage);

		// when
		Page<AttendeeLevelRankingResponseDto> response = adminService.getAttendeeLevelRankings(null,
			pageable);

		// then
		assertThat(response).hasSize(2);
	}

	@DisplayName("참가자 누적 포인트 랭킹을 조회한다.")
	@Test
	void getAttendeePointRankings() {
		// given
		List<Attendee> attendees = List.of(attendee1, attendee2);
		Page<Attendee> attendeePage = new PageImpl<>(attendees, pageable, attendees.size());

		when(attendeeRepository.findAllByOrderByTotalPointsDesc(any(Pageable.class))).thenReturn(attendeePage);

		// when
		Page<AttendeePointRankingResponseDto> response = adminService.getAttendeePointRankings(pageable);

		// then
		assertThat(response).hasSize(2);
		assertThat(response.getContent().get(0).totalPoints())
			.isGreaterThanOrEqualTo(response.getContent().get(1).totalPoints());
	}
}
