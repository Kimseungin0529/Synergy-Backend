package com.synergy.backend.domain.booth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.synergy.backend.domain.booth.dto.BoothResponseDto;
import com.synergy.backend.domain.booth.dto.boothParticipateDto.BoothParticipateRateResDto;
import com.synergy.backend.domain.booth.dto.boothParticipateDto.BoothParticipationInterestedResponseDto;
import com.synergy.backend.domain.booth.entity.Booth;
import com.synergy.backend.domain.booth.entity.BoothParticipation;
import com.synergy.backend.domain.booth.exception.DuplicateParticipationException;
import com.synergy.backend.domain.booth.exception.NotFoundBoothException;
import com.synergy.backend.domain.booth.repository.BoothParticipationRepository;
import com.synergy.backend.domain.booth.repository.BoothRepository;
import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.conference.repository.ConferenceRepository;
import com.synergy.backend.domain.member.entity.Admin;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.repository.AdminRepository;
import com.synergy.backend.domain.member.repository.AttendeeRepository;
import com.synergy.backend.domain.point.service.PointService;
import com.synergy.backend.domain.qrCode.service.QrService;

@ExtendWith(MockitoExtension.class)
class BoothParticipationServiceImplTest {

	@Mock
	private BoothParticipationRepository boothParticipationRepository;
	@Mock
	private BoothRepository boothRepository;
	@Mock
	private ConferenceRepository conferenceRepository;
	@Mock
	private AttendeeRepository attendeeRepository;
	@Mock
	private AdminRepository adminRepository;
	@Mock
	private PointService pointService;
	@Mock
	private QrService qrService;

	@InjectMocks
	private BoothParticipationServiceImpl boothParticipationService;

	@DisplayName("부스 참여 성공 시 BoothResponseDto 반환")
	@Test
	void participateInBooth_success() {
		// Given
		String identifier = "user@example.com";
		Long attendeeId = 10L;
		Long boothId = 1L;
		String secretCode = "encodedCode";
		String decodedCode = "decodedCode";

		Attendee attendee = mock(Attendee.class);
		Booth booth = mock(Booth.class);
		BoothParticipation boothParticipation = mock(BoothParticipation.class);

		when(attendee.getId()).thenReturn(attendeeId);
		when(booth.getId()).thenReturn(boothId);
		when(attendeeRepository.findByEmail(identifier)).thenReturn(Optional.of(attendee));
		when(qrService.decodingSecretCode(secretCode)).thenReturn(decodedCode);
		when(boothRepository.findByIdAndSecretCode(boothId, decodedCode)).thenReturn(Optional.of(booth));
		when(boothParticipationRepository.findByBoothIdAndAttendeeId(boothId, attendee.getId())).thenReturn(
			Optional.empty());
		when(boothParticipationRepository.save(any(BoothParticipation.class))).thenReturn(boothParticipation);
		doNothing().when(pointService).addBoothPoint(attendeeId, boothId);

		// When
		BoothResponseDto result = boothParticipationService.participateInBooth(identifier, boothId, secretCode);

		// Then
		assertEquals(booth.getId(), result.id());
	}

	@DisplayName("이미 참여한 부스일 경우 DuplicateParticipationException 발생")
	@Test
	void participateInBooth_alreadyParticipated_throwsException() {
		// Given
		String identifier = "user@example.com";
		Long boothId = 1L;
		String secretCode = "encodedCode";
		String decodedCode = "decodedCode";

		Attendee attendee = mock(Attendee.class);
		Booth booth = mock(Booth.class);
		BoothParticipation boothParticipation = mock(BoothParticipation.class);

		when(attendee.getId()).thenReturn(10L);
		when(attendeeRepository.findByEmail(identifier)).thenReturn(Optional.of(attendee));
		when(qrService.decodingSecretCode(secretCode)).thenReturn(decodedCode);
		when(boothRepository.findByIdAndSecretCode(boothId, decodedCode)).thenReturn(Optional.of(booth));
		when(boothParticipationRepository.findByBoothIdAndAttendeeId(boothId, attendee.getId()))
			.thenReturn(Optional.of(boothParticipation));

		// When & Then
		assertThrows(DuplicateParticipationException.class,
			() -> boothParticipationService.participateInBooth(identifier, boothId, secretCode));
	}

	@DisplayName("존재하지 않는 부스일 경우 NotFoundBoothException 발생")
	@Test
	void participateInBooth_boothNotFound_throwsException() {
		// Given
		String identifier = "user@example.com";
		Long boothId = 1L;
		String secretCode = "encodedCode";
		String decodedCode = "decodedCode";

		Attendee attendee = mock(Attendee.class);
		when(attendeeRepository.findByEmail(identifier)).thenReturn(Optional.of(attendee));
		when(qrService.decodingSecretCode(secretCode)).thenReturn(decodedCode);
		when(boothRepository.findByIdAndSecretCode(boothId, decodedCode)).thenReturn(Optional.empty());

		// When & Then
		assertThrows(NotFoundBoothException.class,
			() -> boothParticipationService.participateInBooth(identifier, boothId, secretCode));
	}

	@DisplayName("부스 참여율 조회 - 성공")
	@Test
	void boothParticipateRate_success() {
		// Given
		String adminIdentifier = "ADM123";
		Long conferenceId = 1L;
		LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));

		Admin admin = mock(Admin.class);
		BoothParticipateRateResDto responseDto = mock(BoothParticipateRateResDto.class);

		when(admin.getId()).thenReturn(1L);
		when(adminRepository.findByAdminAuthCode(adminIdentifier)).thenReturn(Optional.of(admin));
		when(conferenceRepository.findById(conferenceId)).thenReturn(Optional.of(mock(Conference.class)));
		when(adminRepository.findByIdAndConferences_Id(admin.getId(), conferenceId)).thenReturn(Optional.of(admin));
		when(boothRepository.searchBoothRank(conferenceId, now)).thenReturn(responseDto);

		// When
		BoothParticipateRateResDto result = boothParticipationService.boothParticipateRate(adminIdentifier,
			conferenceId);

		// Then
		assertEquals(responseDto, result);
	}

	@DisplayName("관심도별 부스 참여 횟수 조회 - 성공")
	@Test
	void getParticipationCountByInterest_success() {
		// Given
		Long conferenceId = 1L;
		List<BoothParticipationInterestedResponseDto> expected = List.of(
			mock(BoothParticipationInterestedResponseDto.class));

		when(boothRepository.searchBoothParticipation(conferenceId)).thenReturn(expected);

		// When
		List<BoothParticipationInterestedResponseDto> result = boothParticipationService.getParticipationCountByInterest(
			conferenceId);

		// Then
		assertEquals(expected, result);
	}
}
