package com.synergy.backend.domain.session.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.conference.entity.TimePeriod;
import com.synergy.backend.domain.conference.repository.ConferenceRepository;
import com.synergy.backend.domain.member.entity.Admin;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.domain.member.repository.AdminRepository;
import com.synergy.backend.domain.member.repository.AttendeeRepository;
import com.synergy.backend.domain.qrCode.service.QrService;
import com.synergy.backend.domain.session.dto.questionDto.QuestionResDto;
import com.synergy.backend.domain.session.dto.sessionDto.SessionDetailResDto;
import com.synergy.backend.domain.session.dto.sessionDto.SessionReqDto;
import com.synergy.backend.domain.session.dto.sessionDto.SessionResDto;
import com.synergy.backend.domain.session.entity.AttendeeSession;
import com.synergy.backend.domain.session.entity.Session;
import com.synergy.backend.domain.session.repository.AttendeeSessionRepository;
import com.synergy.backend.domain.session.repository.sessionQuestionRepository.SessionQuestionRepository;
import com.synergy.backend.domain.session.repository.sessionRepository.SessionRepository;
import com.synergy.backend.global.exception.AuthorizedException;
import com.synergy.backend.global.util.file.dto.FileInformationDto;
import com.synergy.backend.global.util.file.util.FileS3Util;

@ExtendWith(MockitoExtension.class)
class SessionServiceImplTest {

	@InjectMocks
	SessionServiceImpl sessionService;

	@Mock
	AdminRepository adminRepository;
	@Mock
	ConferenceRepository conferenceRepository;
	@Mock
	SessionRepository sessionRepository;
	@Mock
	AttendeeSessionRepository attendeeSessionRepository;
	@Mock
	SessionQuestionRepository sessionQuestionRepository;
	@Mock
	AttendeeRepository attendeeRepository;
	@Mock
	FileS3Util fileS3Util;
	@Mock
	QrService qrService;

	@Test
	@DisplayName("createSession - 성공적으로 세션 생성")
	void createSession_shouldCreateSessionSuccessfully() throws Exception {
		// Given
		String identifier = "ADM123";
		String router = "https://example.com";
		Long conferenceId = 1L;
		SessionReqDto reqDto = new SessionReqDto("세션 제목", "홍길동", "기조연설가", LocalDate.of(2025, 6, 10),
			LocalDateTime.of(2025, 6, 10, 14, 0), LocalDateTime.of(2025, 6, 10, 15, 0), "세션 설명", 100);
		MultipartFile mockFile = new MockMultipartFile("file", "test.png", "image/png", "fake-content".getBytes());

		Admin admin = mock(Admin.class);
		Conference conference = mock(Conference.class);
		Session session = mock(Session.class);
		byte[] qrCode = new byte[] {1, 2, 3};
		FileInformationDto qrFileInfo = new FileInformationDto("qr-file-key", "https://s3.com/qr-file");
		FileInformationDto imageFileInfo = new FileInformationDto("img-file-key", "https://s3.com/image");

		SessionServiceImpl service = new SessionServiceImpl(adminRepository, conferenceRepository, sessionRepository,
			attendeeSessionRepository, sessionQuestionRepository, attendeeRepository, fileS3Util, qrService);

		given(adminRepository.findByAdminAuthCode(identifier)).willReturn(Optional.of(admin));
		given(conferenceRepository.findById(conferenceId)).willReturn(Optional.of(conference));
		given(sessionRepository.save(any(Session.class))).willAnswer(invocation -> invocation.getArgument(0));
		given(qrService.generateQRCode(anyString(), anyString())).willReturn(qrCode);
		given(fileS3Util.uploadQRCode(any(), any())).willReturn(qrFileInfo);
		given(fileS3Util.uploadFile(mockFile)).willReturn(imageFileInfo);

		doNothing().when(admin).addSession(any());

		// When
		service.createSession(identifier, router, conferenceId, reqDto, mockFile);

		// Then
		verify(adminRepository).findByAdminAuthCode(identifier);
		verify(conferenceRepository).findById(conferenceId);
		verify(sessionRepository).save(any(Session.class));
		verify(fileS3Util).uploadQRCode(any(), any());
		verify(fileS3Util).uploadFile(mockFile);
		then(sessionRepository).should().save(argThat(savedSession -> {
			assertThat(savedSession.getTitle()).isEqualTo("세션 제목");
			assertThat(savedSession.getSpeaker()).isEqualTo("홍길동");
			assertThat(savedSession.getSpeakerPosition()).isEqualTo("기조연설가");
			assertThat(savedSession.getMaximum()).isEqualTo(100);
			assertThat(savedSession.getStartTime()).isEqualTo(LocalDateTime.of(2025, 6, 10, 14, 0));
			assertThat(savedSession.getEndTime()).isEqualTo(LocalDateTime.of(2025, 6, 10, 15, 0));
			return true;
		}));
	}

	@Test
	@DisplayName("getSessions - 세션 목록 조회 성공")
	void getSessions_shouldReturnSessionList() {
		// Given
		String identifier = "ADM123";
		Long conferenceId = 1L;

		Admin mockAdmin = mock(Admin.class);
		Conference conference = Conference.of(
			"conference1",
			TimePeriod.of(LocalDate.now(), LocalDate.now().plusDays(1), LocalTime.of(9, 0), LocalTime.of(18, 0)),
			"주최자",
			"서울",
			"개발",
			"ONLINE"
		);
		ReflectionTestUtils.setField(conference, "id", conferenceId);
		SessionReqDto reqDto1 = new SessionReqDto(
			"세션1", "발표자1", "직책1",
			LocalDate.of(2025, 6, 10),
			LocalDateTime.of(2025, 6, 10, 14, 0),
			LocalDateTime.of(2025, 6, 10, 15, 0),
			"설명1", 100
		);
		Session session1 = Session.of(reqDto1, "secretCode1", conference);

		SessionReqDto reqDto2 = new SessionReqDto(
			"세션2", "발표자2", "직책2",
			LocalDate.of(2025, 6, 11),
			LocalDateTime.of(2025, 6, 11, 10, 0),
			LocalDateTime.of(2025, 6, 11, 11, 0),
			"설명2", 120
		);
		Session session2 = Session.of(reqDto2, "secretCode2", conference);

		given(conferenceRepository.findById(conferenceId)).willReturn(Optional.of(conference));
		given(sessionRepository.findAllByConferenceOrderByStartTime(conference)).willReturn(
			List.of(session1, session2));

		// When
		List<SessionResDto> result = sessionService.getSessions(identifier, conferenceId);

		// Then
		assertThat(result).hasSize(2);
		assertThat(result.get(0).title()).isEqualTo("세션1");
		assertThat(result.get(1).speaker()).isEqualTo("발표자2");
	}

	@Test
	@DisplayName("getSessionInfo - 세션 상세 조회 성공 (ATTENDEE)")
	void getSessionInfo_shouldReturnSessionDetailForAttendee() {
		// Given
		String identifier = "attendee@email.com";
		RoleType role = RoleType.ATTENDEE;
		Long conferenceId = 1L;
		Long sessionId = 10L;

		Conference conference = Conference.of(
			"conference1",
			TimePeriod.of(LocalDate.now(), LocalDate.now().plusDays(1), LocalTime.of(9, 0), LocalTime.of(18, 0)),
			"주최자",
			"서울",
			"개발",
			"ONLINE"
		);

		Attendee attendee = mock(Attendee.class);
		Session session = mock(Session.class);
		AttendeeSession attendeeSession = mock(AttendeeSession.class);
		QuestionResDto question1 = new QuestionResDto(1L, "참가자1", "질문1");

		given(conferenceRepository.findById(conferenceId)).willReturn(Optional.of(conference));
		given(sessionRepository.findByIdAndConference(sessionId, conference)).willReturn(Optional.of(session));
		given(attendeeRepository.findByEmail(identifier)).willReturn(Optional.of(attendee));
		given(attendee.getId()).willReturn(1L);
		given(attendeeSessionRepository.findBySessionIdAndAttendeeId(sessionId, 1L)).willReturn(
			Optional.of(attendeeSession));
		given(sessionQuestionRepository.findBySessionIdJoinAttendeeSession(sessionId)).willReturn(List.of(question1));

		// When
		SessionDetailResDto result = sessionService.getSessionInfo(identifier, role, conferenceId, sessionId);

		// Then
		assertThat(result.title()).isEqualTo(session.getTitle());
		assertThat(result.questionResDto()).hasSize(1);
		assertThat(result.questionResDto().get(0).content()).isEqualTo("질문1");
		assertThat(result.isQRVerify()).isTrue();
	}

	@Test
	@DisplayName("updateSession - 세션 수정 성공")
	void updateSession_shouldUpdateSessionSuccessfully() {
		// Given
		String identifier = "ADM123";
		Long sessionId = 1L;
		SessionReqDto reqDto = new SessionReqDto("수정된 제목", "홍길동", "CTO",
			LocalDate.of(2025, 6, 12),
			LocalDateTime.of(2025, 6, 12, 10, 0),
			LocalDateTime.of(2025, 6, 12, 11, 0),
			"수정된 설명", 150);
		MultipartFile mockFile = new MockMultipartFile("file", "updated.png", "image/png",
			"updated-content".getBytes());

		Session session = mock(Session.class);
		Admin admin = mock(Admin.class);

		given(session.getId()).willReturn(sessionId);
		given(sessionRepository.findById(sessionId)).willReturn(Optional.of(session));
		given(adminRepository.findByAdminAuthCode(identifier)).willReturn(Optional.of(admin));
		given(sessionRepository.existsByIdAndAdmins_Id(sessionId, admin.getId())).willReturn(true);
		given(fileS3Util.uploadFile(mockFile)).willReturn(new FileInformationDto("key", "url"));

		doNothing().when(session).updateSession(reqDto);
		doNothing().when(session).addImage(any());

		// When
		sessionService.updateSession(identifier, sessionId, reqDto, mockFile);

		// Then
		verify(sessionRepository).findById(sessionId);
		verify(adminRepository).findByAdminAuthCode(identifier);
		verify(fileS3Util).uploadFile(mockFile);
		verify(session).updateSession(reqDto);
	}

	@Test
	@DisplayName("deleteSession - 세션 삭제 성공")
	void deleteSession_shouldDeleteSessionSuccessfully() {
		// Given
		String identifier = "ADM123";
		Long sessionId = 1L;

		Session session = mock(Session.class);
		Admin admin = mock(Admin.class);

		given(session.getId()).willReturn(sessionId);

		given(sessionRepository.findById(sessionId)).willReturn(Optional.of(session));
		given(adminRepository.findByAdminAuthCode(identifier)).willReturn(Optional.of(admin));
		given(sessionRepository.existsByIdAndAdmins_Id(sessionId, admin.getId())).willReturn(true);

		doNothing().when(session).removeAllAdmins();
		doNothing().when(sessionRepository).delete(session);

		// When
		sessionService.deleteSession(identifier, sessionId);

		// Then
		verify(sessionRepository).findById(sessionId);
		verify(adminRepository).findByAdminAuthCode(identifier);
		verify(session).removeAllAdmins();
		verify(sessionRepository).delete(session);
	}

	@Test
	@DisplayName("getSessionInfo - 세션 상세 조회 실패 (ATTENDEE가 해당 세션 참석 안함)")
	void getSessionInfo_shouldReturnEmptyQuestionsWhenAttendeeNotParticipated() {
		// Given
		String identifier = "attendee@email.com";
		RoleType role = RoleType.ATTENDEE;
		Long conferenceId = 1L;
		Long sessionId = 10L;

		Conference conference = Conference.of(
			"conference1",
			TimePeriod.of(LocalDate.now(), LocalDate.now().plusDays(1), LocalTime.of(9, 0), LocalTime.of(18, 0)),
			"주최자",
			"서울",
			"개발",
			"ONLINE"
		);

		Attendee attendee = mock(Attendee.class);
		Session session = mock(Session.class);

		given(conferenceRepository.findById(conferenceId)).willReturn(Optional.of(conference));
		given(sessionRepository.findByIdAndConference(sessionId, conference)).willReturn(Optional.of(session));
		given(attendeeRepository.findByEmail(identifier)).willReturn(Optional.of(attendee));
		given(attendee.getId()).willReturn(1L);
		given(attendeeSessionRepository.findBySessionIdAndAttendeeId(sessionId, 1L)).willReturn(Optional.empty());

		// When
		SessionDetailResDto result = sessionService.getSessionInfo(identifier, role, conferenceId, sessionId);

		// Then
		assertThat(result.title()).isEqualTo(session.getTitle());
		assertThat(result.questionResDto()).isEmpty();
		assertThat(result.isQRVerify()).isFalse();
	}

	@Test
	@DisplayName("세션 수정 - 권한이 없는 관리자일 경우 예외 발생")
	void updateSession_shouldThrowExceptionWhenUnauthorizedAdmin() {
		// Given
		String identifier = "ADM123";
		Long sessionId = 1L;
		SessionReqDto reqDto = new SessionReqDto("제목", "홍길동", "CTO",
			LocalDate.of(2025, 6, 12),
			LocalDateTime.of(2025, 6, 12, 10, 0),
			LocalDateTime.of(2025, 6, 12, 11, 0),
			"설명", 150);
		MultipartFile mockFile = new MockMultipartFile("file", "file.png", "image/png", "fake".getBytes());

		Session session = mock(Session.class);
		Admin admin = mock(Admin.class);

		given(session.getId()).willReturn(sessionId);
		given(admin.getId()).willReturn(99L);
		given(sessionRepository.findById(sessionId)).willReturn(Optional.of(session));
		given(adminRepository.findByAdminAuthCode(identifier)).willReturn(Optional.of(admin));
		given(sessionRepository.existsByIdAndAdmins_Id(sessionId, 99L)).willReturn(false);

		// When & Then
		assertThatThrownBy(() -> sessionService.updateSession(identifier, sessionId, reqDto, mockFile))
			.isInstanceOf(AuthorizedException.class);

		verify(sessionRepository).findById(sessionId);
		verify(adminRepository).findByAdminAuthCode(identifier);
		verify(sessionRepository).existsByIdAndAdmins_Id(sessionId, 99L);
	}

	@Test
	@DisplayName("getSessionInfo - 관리자(ADMIN) 요청 시 질문 없이 세션 정보 반환")
	void getSessionInfo_shouldReturnSessionDetailForAdmin() {
		// Given
		String identifier = "admin@email.com";
		RoleType role = RoleType.ADMIN;
		Long conferenceId = 1L;
		Long sessionId = 10L;

		Conference conference = Conference.of(
			"conference1",
			TimePeriod.of(LocalDate.now(), LocalDate.now().plusDays(1), LocalTime.of(9, 0), LocalTime.of(18, 0)),
			"주최자",
			"서울",
			"개발",
			"ONLINE"
		);

		Session session = mock(Session.class);

		given(conferenceRepository.findById(conferenceId)).willReturn(Optional.of(conference));
		given(sessionRepository.findByIdAndConference(sessionId, conference)).willReturn(Optional.of(session));
		given(sessionQuestionRepository.findBySessionIdJoinAttendeeSession(sessionId)).willReturn(List.of());

		// When
		SessionDetailResDto result = sessionService.getSessionInfo(identifier, role, conferenceId, sessionId);

		// Then
		assertThat(result.title()).isEqualTo(session.getTitle());
		assertThat(result.questionResDto()).isEmpty();
		assertThat(result.isQRVerify()).isFalse(); // ADMIN은 기본적으로 QR 검증 대상이 아님
	}

}
