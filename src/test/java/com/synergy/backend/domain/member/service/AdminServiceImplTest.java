package com.synergy.backend.domain.member.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
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
import org.springframework.test.util.ReflectionTestUtils;

import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.conference.entity.TimePeriod;
import com.synergy.backend.domain.member.api.dto.response.AttendeeLevelRankingResponseDto;
import com.synergy.backend.domain.member.api.dto.response.AttendeePointRankingResponseDto;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.details.MembershipLevelType;
import com.synergy.backend.domain.member.repository.AttendeeRedisRankingRepository;
import com.synergy.backend.domain.member.repository.AttendeeRepository;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@Slf4j
class AdminServiceImplTest {

	@Mock
	AttendeeRepository attendeeRepository;

	@Mock
	AttendeeRedisRankingRepository attendeeRedisRankingRepository;

	@InjectMocks
	AdminServiceImpl adminService;

	private Attendee attendee1;
	private Attendee attendee2;
	private PageRequest pageable;

	@BeforeEach
	void setUp() {
		Conference conference = Conference.of(
			"conference1",
			TimePeriod.of(LocalDate.now(), LocalDate.now().plusDays(1), LocalTime.of(9, 0), LocalTime.of(18, 0)),
			"주최자",
			"서울",
			"개발",
			"ONLINE"
		);
		ReflectionTestUtils.setField(conference, "id", 1L);

		attendee1 = Attendee.of("user1@email.com", "pass", "user1", "010110");
		attendee1.addTotalPoints(1000);
		attendee1.assignConference(conference);
		assertThat(attendee1.getMembershipLevelType()).isEqualTo(MembershipLevelType.GOLD);

		attendee2 = Attendee.of("user2@email.com", "pass", "user2", "010110");
		attendee2.addTotalPoints(50);
		attendee2.assignConference(conference);
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
		Page<AttendeeLevelRankingResponseDto> response = adminService.getAttendeeLevelRankings(1L,
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

		when(attendeeRepository.findByConferenceIdOrderByTotalPointsDesc(eq(1L), any(Pageable.class))).thenReturn(
			attendeePage);

		// when
		Page<AttendeeLevelRankingResponseDto> response = adminService.getAttendeeLevelRankings(1L, null,
			pageable);

		// then
		assertThat(response).hasSize(2);
	}

	@DisplayName("참가자 누적 포인트 랭킹을 조회한다.")
	@Test
	void getAttendeePointRankings() {
		// given
		AttendeePointRankingResponseDto dto1 = AttendeePointRankingResponseDto.from(attendee1);
		AttendeePointRankingResponseDto dto2 = AttendeePointRankingResponseDto.from(attendee2);

		List<AttendeePointRankingResponseDto> dtoList = List.of(dto1, dto2);
		Page<AttendeePointRankingResponseDto> rankingPage = new PageImpl<>(dtoList, pageable, dtoList.size());

		when(attendeeRedisRankingRepository.getRankingPage(eq(1L), any(Pageable.class))).thenReturn(rankingPage);

		// when
		Page<AttendeePointRankingResponseDto> response = adminService.getAttendeePointRankings(1L, pageable);

		// then
		assertThat(response).hasSize(2);
		assertThat(response.getContent().get(0).totalPoints())
			.isGreaterThanOrEqualTo(response.getContent().get(1).totalPoints());
	}

	@DisplayName("캐시 미스 시 DB에서 1000명을 조회하고 Redis에 저장 후 다시 조회한다.")
	@Test
	void getAttendeePointRankings_cacheMissThenSaveThenRead() {
		// given
		Page<AttendeePointRankingResponseDto> emptyPage = new PageImpl<>(List.of(), pageable, 0);
		when(attendeeRedisRankingRepository.getRankingPage(eq(1L), any(Pageable.class)))
			.thenReturn(emptyPage) // 첫 번째 호출: 캐시 미스
			.thenReturn(new PageImpl<>(List.of(
				AttendeePointRankingResponseDto.from(attendee1),
				AttendeePointRankingResponseDto.from(attendee2)
			), pageable, 2)); // 두 번째 호출: 저장 후 재조회

		Page<AttendeePointRankingResponseDto> dbResultPage = new PageImpl<>(
			List.of(
				AttendeePointRankingResponseDto.from(attendee1),
				AttendeePointRankingResponseDto.from(attendee2)
			), PageRequest.of(0, 1000), 2
		);
		when(attendeeRepository.findTopAttendeeRankingsDtoByConferenceId(eq(1L), any(Pageable.class)))
			.thenReturn(dbResultPage);

		// when
		Page<AttendeePointRankingResponseDto> response = adminService.getAttendeePointRankings(1L, pageable);

		// then
		assertThat(response).hasSize(2);
		verify(attendeeRepository).findTopAttendeeRankingsDtoByConferenceId(eq(1L), any(Pageable.class));
		verify(attendeeRedisRankingRepository, times(2)).getRankingPage(eq(1L), any(Pageable.class));
		verify(attendeeRedisRankingRepository, times(2)).saveRanking(eq(1L), any());
	}
}
