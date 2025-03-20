package com.synergy.backend.domain.member.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.synergy.backend.domain.interest.repository.AttendeeInterestRepository;
import com.synergy.backend.domain.interest.repository.InterestRepository;
import com.synergy.backend.domain.job.JobCategoryRepository;
import com.synergy.backend.domain.job.OccupationCategoryRepository;
import com.synergy.backend.domain.member.api.dto.resposne.AttendeeInfoDetailResponseDto;
import com.synergy.backend.domain.member.api.dto.resposne.MyInfoResponseDto;
import com.synergy.backend.domain.member.entity.Admin;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.domain.member.repository.AttendeeRepository;
import com.synergy.backend.domain.point.entity.Point;
import com.synergy.backend.domain.point.entity.PointType;
import com.synergy.backend.domain.point.repository.PointRepository;
import com.synergy.backend.domain.point.service.PointService;

@ExtendWith(MockitoExtension.class)
class AttendeeServiceImplTest {

	@Mock
	private AttendeeRepository attendeeRepository;
	@Mock
	private InterestRepository interestRepository;
	@Mock
	private AttendeeInterestRepository attendeeInterestRepository;
	@Mock
	private JobCategoryRepository jobCategoryRepository;
	@Mock
	private OccupationCategoryRepository occupationCategoryRepository;
	@Mock
	private PointRepository pointRepository;
	@Mock
	private PointService pointService;

	@InjectMocks
	private AttendeeServiceImpl attendeeService;

	private Attendee attendee;
	private Attendee attendee2;
	private Admin admin;

	@BeforeEach
	void setUp() {
		attendee = Attendee.of("email@example.com", "password", "Tester", "01012345678");
		attendee2 = Attendee.of("email2@example.com", "password", "Tester2", "01012345678");
		admin = Admin.of("ADMIN1234");
	}

	@DisplayName("관리자가 참가자 정보를 조회한다.")
	@Test
	void getAttendeeInfoDetailByAdmin() {
		// given
		when(attendeeRepository.findById(1L)).thenReturn(Optional.ofNullable(attendee));

		// when
		AttendeeInfoDetailResponseDto response = attendeeService.getAttendeeInfoDetail(1L, "ADMIN1234",
			RoleType.ADMIN);

		// then
		assertThat(response).isNotNull();
		assertEquals(attendee.getName(), response.name());
	}

	@DisplayName("참가자가 내 정보를 조회한다.")
	@Test
	void getMyInformation() {
		// given
		String identifier = "email@example.com";
		List<Point> pointList = List.of(Point.of(PointType.SIGN_UP), Point.of(PointType.BOOTH_VISIT));

		when(attendeeRepository.findByEmail(identifier)).thenReturn(Optional.of(attendee));
		when(pointRepository.findRecentPointsByAttendeeId(attendee.getId())).thenReturn(pointList);

		// when
		MyInfoResponseDto response = attendeeService.getMyInformation(identifier);

		// then
		assertThat(response).isNotNull();
		assertThat(response.recentPoints()).hasSize(2);
		assertEquals(attendee.getTotalPoints(), response.totalPoints());
	}

	@Test
	@DisplayName("참가자가 포인트가 없어도 정보를 정상적으로 조회한다.")
	void getMyInformation_NoPoints() {
		// given
		String identifier = "email@example.com";

		when(attendeeRepository.findByEmail(identifier)).thenReturn(Optional.of(attendee));
		when(pointRepository.findRecentPointsByAttendeeId(attendee.getId())).thenReturn(Collections.emptyList());

		// when
		MyInfoResponseDto response = attendeeService.getMyInformation(identifier);

		// then
		assertThat(response).isNotNull();
		assertThat(response.recentPoints()).isEmpty();
	}
}
