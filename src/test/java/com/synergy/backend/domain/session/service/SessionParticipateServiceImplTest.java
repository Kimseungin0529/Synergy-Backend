package com.synergy.backend.domain.session.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.conference.repository.ConferenceRepository;
import com.synergy.backend.domain.member.entity.Admin;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.repository.AdminRepository;
import com.synergy.backend.domain.member.repository.AttendeeRepository;
import com.synergy.backend.domain.point.service.PointService;
import com.synergy.backend.domain.qrCode.service.QrService;
import com.synergy.backend.domain.session.dto.questionDto.QuestionReqDto;
import com.synergy.backend.domain.session.dto.sessionDto.SessionResDto;
import com.synergy.backend.domain.session.dto.sessionparticipateDto.SessionParticipateRateDetailResDto;
import com.synergy.backend.domain.session.dto.sessionparticipateDto.SessionParticipateRateResDto;
import com.synergy.backend.domain.session.dto.sessionparticipateDto.SessionParticipateTechResDto;
import com.synergy.backend.domain.session.entity.AttendeeSession;
import com.synergy.backend.domain.session.entity.Session;
import com.synergy.backend.domain.session.exception.AlreadyAttendedException;
import com.synergy.backend.domain.session.repository.AttendeeSessionRepository;
import com.synergy.backend.domain.session.repository.sessionQuestionRepository.SessionQuestionRepository;
import com.synergy.backend.domain.session.repository.sessionRepository.SessionRepository;

@ExtendWith(MockitoExtension.class)
class SessionParticipateServiceImplTest {

	@InjectMocks
	SessionParticipateServiceImpl sessionParticipateService;

	@Mock
	QrService qrService;
	@Mock
	SessionRepository sessionRepository;
	@Mock
	SessionQuestionRepository sessionQuestionRepository;
	@Mock
	ConferenceRepository conferenceRepository;
	@Mock
	AttendeeSessionRepository attendeeSessionRepository;
	@Mock
	AttendeeRepository attendeeRepository;
	@Mock
	AdminRepository adminRepository;
	@Mock
	PointService pointService;

	@Test
	@DisplayName("QR코드를 인증하면 세션 참여가 저장된다")
	void verifyQRCode_shouldVerifyAndSaveSessionParticipation() {
		// given
		String identifier = "test@email.com";
		Long sessionId = 1L;
		String secretCode = "encodedSecret";
		String decodedSecret = "decodedSecret";

		Attendee attendee = mock(Attendee.class);
		Session session = mock(Session.class);
		AttendeeSession attendeeSession = mock(AttendeeSession.class);

		given(attendeeRepository.findByEmail(identifier)).willReturn(Optional.of(attendee));
		given(qrService.decodingSecretCode(secretCode)).willReturn(decodedSecret);
		given(sessionRepository.findByIdAndSecretCode(sessionId, decodedSecret)).willReturn(Optional.of(session));
		given(attendee.getId()).willReturn(1L);
		given(attendeeSessionRepository.findBySessionIdAndAttendeeId(sessionId, 1L)).willReturn(Optional.empty());
		given(session.getId()).willReturn(1L);

		// when
		SessionResDto result = sessionParticipateService.verifyQRCode(identifier, sessionId, secretCode);

		// then
		verify(attendeeSessionRepository).save(any(AttendeeSession.class));
		verify(pointService).addSessionAttendPoint(1L, 1L);
		assertThat(result).isNotNull();
		assertThat(result.id()).isEqualTo(session.getId());
	}

	@Test
	@DisplayName("세션 질문을 생성하면 포인트가 적립된다")
	void createQuestion_shouldSaveSessionQuestionAndAddPoints() {
		// given
		String identifier = "test@email.com";
		Long sessionId = 1L;
		Long attendeeId = 10L;
		String questionContent = "질문 내용입니다.";

		Attendee attendee = mock(Attendee.class);
		AttendeeSession attendeeSession = mock(AttendeeSession.class);
		QuestionReqDto questionReqDto = new QuestionReqDto(questionContent);

		given(attendeeRepository.findByEmail(identifier)).willReturn(Optional.of(attendee));
		given(attendee.getId()).willReturn(attendeeId);
		given(attendeeSessionRepository.findBySessionIdAndAttendeeId(sessionId, attendeeId)).willReturn(
			Optional.of(attendeeSession));

		// when
		sessionParticipateService.createQuestion(identifier, sessionId, questionReqDto);

		// then
		verify(sessionQuestionRepository).save(any());
		verify(attendeeSession).addSessionQuestion(any());
		verify(pointService).addSessionQnaPoint(attendeeId, sessionId);
	}

	@Test
	@DisplayName("세션 참여율을 조회하면 참여 정보가 반환된다")
	void getSessionParticipateRate_shouldReturnSessionRates() {
		// given
		String identifier = "admin-auth-code";
		Long conferenceId = 1L;

		Admin admin = mock(Admin.class);
		List<SessionParticipateRateResDto> mockRates = List.of(
			new SessionParticipateRateResDto("Session 1", LocalDate.now(), 10L, 30),
			new SessionParticipateRateResDto("Session 2", LocalDate.now(), 5L, 20)
		);

		given(adminRepository.findByAdminAuthCode(identifier)).willReturn(Optional.of(admin));
		given(conferenceRepository.findById(conferenceId)).willReturn(Optional.of(mock(Conference.class)));
		given(sessionRepository.getSessionParticipateByConferenceId(eq(conferenceId), any())).willReturn(mockRates);

		// when
		List<SessionParticipateRateResDto> result = sessionParticipateService.getSessionParticipateRate(identifier,
			conferenceId);

		// then
		assertThat(result).hasSize(2);
		assertThat(result.get(0).title()).isEqualTo("Session 1");
		assertThat(result.get(1).title()).isEqualTo("Session 2");
	}

	@Test
	@DisplayName("세션 참여 상세 정보를 조회하면 기술 통계가 포함된 리스트가 반환된다")
	void getSessionParticipateRateDetail_shouldReturnDetailedParticipationInfo() {
		// given
		String identifier = "admin-auth-code";
		Long conferenceId = 1L;

		Admin admin = mock(Admin.class);
		List<SessionParticipateRateDetailResDto> mockRawList = List.of(
			new SessionParticipateRateDetailResDto(
				1L,
				"세션 제목",
				"발표자",
				"발표자 직책",
				"세션 설명",
				100,
				LocalDate.now(),
				LocalDateTime.now(),
				LocalDateTime.now(),
				"https://example.com/qr",
				List.of(
					new SessionParticipateTechResDto("Java", 10L),
					new SessionParticipateTechResDto("Spring", 8L),
					new SessionParticipateTechResDto("JPA", 5L)
				)
			)
		);

		given(adminRepository.findByAdminAuthCode(identifier)).willReturn(Optional.of(admin));
		given(sessionRepository.getSessionParticipateDetailByConferenceId(conferenceId)).willReturn(mockRawList);

		// when
		List<SessionParticipateRateDetailResDto> result = sessionParticipateService.getSessionParticipateRateDetail(
			identifier, conferenceId);

		// then
		assertThat(result).hasSize(1);
		assertThat(result.get(0).title()).isEqualTo("세션 제목");
		assertThat(result.get(0).dataset()).hasSize(3);
		assertThat(result.get(0).dataset().get(0).tech()).isEqualTo("Java");

	}

	@Test
	@DisplayName("기술 통계가 6개를 초과하면 상위 6개와 기타를 반환한다")
	void refineTechList_shouldReturnTop6AndEtc() {
		// given
		List<SessionParticipateTechResDto> techList = List.of(
			new SessionParticipateTechResDto("Java", 10L),
			new SessionParticipateTechResDto("Spring", 9L),
			new SessionParticipateTechResDto("JPA", 8L),
			new SessionParticipateTechResDto("React", 7L),
			new SessionParticipateTechResDto("Node", 6L),
			new SessionParticipateTechResDto("Python", 5L),
			new SessionParticipateTechResDto("Docker", 4L),
			new SessionParticipateTechResDto("Kubernetes", 3L)
		);

		SessionParticipateRateDetailResDto inputDto = new SessionParticipateRateDetailResDto(
			1L, "세션1", "발표자", "CTO", "설명", 100,
			LocalDate.now(), LocalDateTime.now(), LocalDateTime.now(),
			"https://example.com", techList
		);

		// when
		SessionParticipateRateDetailResDto result = new SessionParticipateServiceImpl(
			qrService, sessionRepository, sessionQuestionRepository,
			conferenceRepository, attendeeSessionRepository, attendeeRepository,
			adminRepository, pointService
		).refineTechList(inputDto);

		// then
		List<SessionParticipateTechResDto> resultDataset = result.dataset();
		assertThat(resultDataset).hasSize(7); // 6개 + 기타
		assertThat(resultDataset.get(6).tech()).isEqualTo("기타");
		assertThat(resultDataset.get(6).attendeeCount()).isEqualTo(7); // Docker + Kubernetes
	}

	@Test
	@DisplayName("이미 참여한 세션의 QR코드를 인증하면 예외가 발생한다")
	void verifyQRCode_shouldThrowExceptionIfAlreadyAttended() {
		// given
		String identifier = "test@email.com";
		Long sessionId = 1L;
		String secretCode = "encodedSecret";
		String decodedSecret = "decodedSecret";

		Attendee attendee = mock(Attendee.class);
		Session session = mock(Session.class);
		AttendeeSession alreadyAttended = mock(AttendeeSession.class);

		given(attendeeRepository.findByEmail(identifier)).willReturn(Optional.of(attendee));
		given(qrService.decodingSecretCode(secretCode)).willReturn(decodedSecret);
		given(sessionRepository.findByIdAndSecretCode(sessionId, decodedSecret)).willReturn(Optional.of(session));
		given(attendee.getId()).willReturn(1L);
		given(attendeeSessionRepository.findBySessionIdAndAttendeeId(sessionId, 1L)).willReturn(
			Optional.of(alreadyAttended));

		// when & then
		assertThatThrownBy(() -> sessionParticipateService.verifyQRCode(identifier, sessionId, secretCode))
			.isInstanceOf(AlreadyAttendedException.class);
	}

	@Test
	@DisplayName("기술 통계가 6개 이하일 경우 원본 리스트를 그대로 반환한다")
	void refineTechList_shouldReturnOriginalIfSixOrLess() {
		// given
		List<SessionParticipateTechResDto> techList = List.of(
			new SessionParticipateTechResDto("Java", 10L),
			new SessionParticipateTechResDto("Spring", 9L),
			new SessionParticipateTechResDto("JPA", 8L),
			new SessionParticipateTechResDto("React", 7L),
			new SessionParticipateTechResDto("Node", 6L),
			new SessionParticipateTechResDto("Python", 5L)
		);

		SessionParticipateRateDetailResDto inputDto = new SessionParticipateRateDetailResDto(
			1L, "세션1", "발표자", "CTO", "설명", 100,
			LocalDate.now(), LocalDateTime.now(), LocalDateTime.now(),
			"https://example.com", techList
		);

		// when
		SessionParticipateRateDetailResDto result = new SessionParticipateServiceImpl(
			qrService, sessionRepository, sessionQuestionRepository,
			conferenceRepository, attendeeSessionRepository, attendeeRepository,
			adminRepository, pointService
		).refineTechList(inputDto);

		// then
		assertThat(result.dataset()).isEqualTo(techList);
	}

	@Test
	@DisplayName("기술 통계 리스트가 null일 경우 원본 DTO를 그대로 반환한다")
	void refineTechList_shouldReturnOriginalIfNull() {
		// given
		SessionParticipateRateDetailResDto inputDto = new SessionParticipateRateDetailResDto(
			1L, "세션1", "발표자", "CTO", "설명", 100,
			LocalDate.now(), LocalDateTime.now(), LocalDateTime.now(),
			"https://example.com", null
		);

		// when
		SessionParticipateRateDetailResDto result = new SessionParticipateServiceImpl(
			qrService, sessionRepository, sessionQuestionRepository,
			conferenceRepository, attendeeSessionRepository, attendeeRepository,
			adminRepository, pointService
		).refineTechList(inputDto);

		// then
		assertThat(result.dataset()).isNull();
	}

}
