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

import com.synergy.backend.domain.booth.entity.Booth;
import com.synergy.backend.domain.booth.repository.BoothRepository;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.Recruiter;
import com.synergy.backend.domain.member.repository.AttendeeRepository;
import com.synergy.backend.domain.member.repository.RecruiterRepository;
import com.synergy.backend.domain.point.api.dto.PointResponseDto;
import com.synergy.backend.domain.point.entity.Point;
import com.synergy.backend.domain.point.entity.PointType;
import com.synergy.backend.domain.point.repository.PointRepository;
import com.synergy.backend.domain.session.entity.Session;
import com.synergy.backend.domain.session.repository.sessionRepository.SessionRepository;

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

	@DisplayName("포인트 ID로 포인트 응답을 조회할 수 있다.")
	@Test
	void testGetPointResponse() {
		// given
		when(pointRepository.findById(1L)).thenReturn(Optional.of(testPoint));

		// when
		PointResponseDto response = pointService.getPointResponse(1L);

		// then
		assertNotNull(response);
		assertEquals(testPoint.getPointType().getMessage(), response.title());
	}

	@DisplayName("사용자의 포인트 응답 리스트를 조회할 수 있다.")
	@Test
	void testGetPointResponses() {
		// given
		when(pointRepository.findByAttendeeIdOrderByCreatedTimeDesc(1L)).thenReturn(List.of(testPoint));
		when(pointRepository.findById(any())).thenReturn(Optional.of(testPoint));

		// when
		List<PointResponseDto> responses = pointService.getPointResponses(1L);

		// then
		assertEquals(1, responses.size());
		assertEquals(testPoint.getPointType().getMessage(), responses.get(0).title());
	}

	@DisplayName("포인트 타입이 부스참여인 경우 포인트 상세정보로 부스 회사명을 반환한다.")
	@Test
	void testGetDetailsForPoint() {
		// given
		Point boothPoint = Point.builder().pointType(PointType.BOOTH_VISIT).boothId(10L).build();
		Booth booth = mock(Booth.class);
		when(booth.getCompanyName()).thenReturn("테스트회사");
		when(boothRepository.findById(10L)).thenReturn(Optional.of(booth));

		// when
		String result = pointService.getDetailsForPoint(boothPoint);

		// then
		assertEquals("테스트회사", result);
	}

	@DisplayName("포인트 타입이 세션참여 또는 QnA인 경우 포인트 상세정보로 세션 제목을 반환한다.")
	@Test
	void testGetDetailsForPoint_SessionTypes() {
		// given
		Point sessionPoint = Point.builder().pointType(PointType.SESSION_ATTEND).sessionId(20L).build();
		Session session = mock(Session.class);
		when(session.getTitle()).thenReturn("세션 제목");
		when(sessionRepository.findById(20L)).thenReturn(Optional.of(session));

		// when
		String result = pointService.getDetailsForPoint(sessionPoint);

		// then
		assertEquals("세션 제목", result);
	}

	@DisplayName("포인트 타입이 채용담당자 미팅인 경우 채용담당자 회사명을 반환한다.")
	@Test
	void testGetDetailsForPoint_RecruiterMeeting() {
		// given
		Point point = Point.builder().pointType(PointType.RECRUITER_MEETING).recruiterId(30L).build();
		Recruiter recruiter = mock(Recruiter.class);
		when(recruiter.getCompany()).thenReturn("채용담당자회사");
		when(recruiterRepository.findById(30L)).thenReturn(Optional.of(recruiter));

		// when
		String result = pointService.getDetailsForPoint(point);

		// then
		assertEquals("채용담당자회사", result);
	}

	@DisplayName("포인트 타입이 회원가입인 경우 정해진 메시지를 반환한다.")
	@Test
	void testGetDetailsForPoint_SignUp() {
		// given
		Point point = Point.builder().pointType(PointType.SIGN_UP).build();

		// when
		String result = pointService.getDetailsForPoint(point);

		// then
		assertEquals("회원가입 적립", result);
	}

	@DisplayName("포인트 타입이 설문조사 참여인 경우 정해진 메시지를 반환한다.")
	@Test
	void testGetDetailsForPoint_SurveyParticipation() {
		// given
		Point point = Point.builder().pointType(PointType.SURVEY_PARTICIPATION).build();

		// when
		String result = pointService.getDetailsForPoint(point);

		// then
		assertEquals("설문조사 참여 적립", result);
	}

	@DisplayName("포인트 타입이 컨텐츠 공유인 경우 정해진 메시지를 반환한다.")
	@Test
	void testGetDetailsForPoint_ContentShare() {
		// given
		Point point = Point.builder().pointType(PointType.CONTENT_SHARE).build();

		// when
		String result = pointService.getDetailsForPoint(point);

		// then
		assertEquals("컨텐츠 공유 적립", result);
	}

}
