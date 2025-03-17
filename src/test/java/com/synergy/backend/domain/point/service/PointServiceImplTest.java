package com.synergy.backend.domain.point.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.synergy.backend.domain.booth.repository.BoothRepository;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.repository.AttendeeRepository;
import com.synergy.backend.domain.member.repository.RecruiterRepository;
import com.synergy.backend.domain.point.entity.Point;
import com.synergy.backend.domain.point.entity.PointType;
import com.synergy.backend.domain.point.repository.PointRepository;
import com.synergy.backend.domain.session.repository.SessionRepository;

@ExtendWith(MockitoExtension.class)
class PointServiceImplTest {

	@InjectMocks
	private PointServiceImpl pointService;

	@Mock
	private PointRepository pointRepository;

	@Mock
	private AttendeeRepository attendeeRepository;

	@Mock
	private BoothRepository boothRepository;

	@Mock
	private SessionRepository sessionRepository;

	@Mock
	private RecruiterRepository recruiterRepository;

	private Attendee testAttendee;
	private Point testPoint;

	@BeforeEach
	void setUp() {
		testAttendee = Attendee.of("email", "pass", "name", "0101");
		testPoint = Point.of(PointType.SIGN_UP);
	}

	@DisplayName("사용자의 포인트 내역을 조회할 수 있다.")
	@Test
	void testGetPointHistory() {
		// given
		List<Point> points = List.of(testPoint);
		when(pointRepository.findByAttendeeIdOrderByCreatedTimeDesc(1L)).thenReturn(points);

		// when
		List<Point> result = pointService.getPointHistory(1L);

		// then
		assertEquals(1, result.size());
		assertEquals(testPoint, result.get(0));
	}

	@DisplayName("부스 참여 타입의 포인트가 추가된다.")
	@Test
	void testAddBoothAttendPoint() {
		// given
		when(attendeeRepository.findById(1L)).thenReturn(Optional.of(testAttendee));

		// when
		pointService.addBoothPoint(1L, 200L);

		// then
		verify(pointRepository, times(1)).save(any(Point.class));
		verify(attendeeRepository, times(1)).save(testAttendee);
		assertEquals(1, testAttendee.getPoints().size());
		assertEquals(PointType.BOOTH_VISIT, testAttendee.getPoints().get(0).getPointType());
	}

	@DisplayName("세션 참여 타입의 포인트가 추가된다.")
	@Test
	void testAddSessionAttendPoint() {
		// given
		when(attendeeRepository.findById(1L)).thenReturn(Optional.of(testAttendee));

		// when
		pointService.addSessionAttendPoint(1L, 200L);

		// then
		verify(pointRepository, times(1)).save(any(Point.class));
		verify(attendeeRepository, times(1)).save(testAttendee);
		assertEquals(1, testAttendee.getPoints().size());
		assertEquals(PointType.SESSION_ATTEND, testAttendee.getPoints().get(0).getPointType());
	}

	@DisplayName("세션 QnA 타입의 포인트가 추가된다.")
	@Test
	void testAddSessionQnAPoint() {
		// given
		when(attendeeRepository.findById(1L)).thenReturn(Optional.of(testAttendee));

		// when
		pointService.addSessionQnaPoint(1L, 200L);

		// then
		verify(pointRepository, times(1)).save(any(Point.class));
		verify(attendeeRepository, times(1)).save(testAttendee);
		assertEquals(1, testAttendee.getPoints().size());
		assertEquals(PointType.SESSION_QNA, testAttendee.getPoints().get(0).getPointType());
	}

	@DisplayName("회원가입 타입의 포인트가 추가된다.")
	@Test
	void testAddSignupPoint() {
		// given
		when(attendeeRepository.findById(1L)).thenReturn(Optional.of(testAttendee));

		// when
		pointService.addSignupPoint(1L);

		// then
		verify(pointRepository, times(1)).save(any(Point.class));
		verify(attendeeRepository, times(1)).save(testAttendee);
		assertEquals(1, testAttendee.getPoints().size());
		assertEquals(PointType.SIGN_UP, testAttendee.getPoints().get(0).getPointType());
	}

}
