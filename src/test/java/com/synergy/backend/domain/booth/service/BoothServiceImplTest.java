package com.synergy.backend.domain.booth.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.when;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.anyString;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.zxing.WriterException;
import com.synergy.backend.domain.booth.dto.BoothDetailResponseDto;
import com.synergy.backend.domain.booth.dto.BoothRequestDto;
import com.synergy.backend.domain.booth.dto.BoothResponseDto;
import com.synergy.backend.domain.booth.entity.Booth;
import com.synergy.backend.domain.booth.exception.NotFoundBoothException;
import com.synergy.backend.domain.booth.repository.BoothParticipationRepository;
import com.synergy.backend.domain.booth.repository.BoothRepository;
import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.conference.repository.ConferenceRepository;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.domain.member.repository.AttendeeRepository;
import com.synergy.backend.domain.qrCode.service.QrService;
import com.synergy.backend.global.jwt.JwtProvider;
import com.synergy.backend.global.security.CustomUserDetailsService;
import com.synergy.backend.global.util.file.dto.FileInformationDto;
import com.synergy.backend.global.util.file.util.FileS3Util;

@ExtendWith(MockitoExtension.class)
public class BoothServiceImplTest {

	@Mock
	AttendeeRepository attendeeRepository;
	@Mock
	BoothParticipationRepository boothParticipationRepository;
	@MockitoBean
	JwtProvider jwtProvider;
	@MockitoBean
	CustomUserDetailsService userDetailsService;
	@Mock
	private BoothRepository boothRepository;
	@Mock
	private ConferenceRepository conferenceRepository;
	@Mock
	private QrService qrService;
	@Mock
	private FileS3Util fileS3Util;
	@Mock
	private MultipartFile mockImageFile;
	@InjectMocks
	private BoothServiceImpl boothService;

	@DisplayName("부스를 생성합니다.")
	@Test
	void createBooth() throws WriterException {
		// given
		String router = "booth/" + 1;
		Long conferenceId = 1L;
		LocalDate progressDate = LocalDate.of(3012, 11, 11);
		BoothRequestDto request = new BoothRequestDto("부스A", "회사A", progressDate, "위치A", "101", "설명A");
		Conference conference = mock(Conference.class);
		String secretCode = "secretCode";

		Booth booth = new Booth(request.companyName(), request.companyType(), request.boothLocation(),
			request.boothNumber(), request.progressDate(), secretCode, request.boothDescription(), conference);
		ReflectionTestUtils.setField(booth, "id", 10L);

		FileInformationDto qrDto = new FileInformationDto("qr-key", "qr-url");
		FileInformationDto imageDto = new FileInformationDto("image-key", "image-url");

		given(conferenceRepository.findById(conferenceId)).willReturn(Optional.of(conference));
		given(boothRepository.save(any(Booth.class))).willReturn(booth);
		given(qrService.generateQRCode(anyString(), anyString())).willReturn(new byte[] {1, 2, 3});
		given(fileS3Util.uploadQRCode(any(), anyString())).willReturn(qrDto);
		given(fileS3Util.uploadFile(any())).willReturn(imageDto);

		// when
		boothService.createBooth(conferenceId, router, request, mockImageFile);

		// then
		assertThat(boothRepository).isNotNull();
	}

	@DisplayName("컨퍼런스 ID로 모든 부스를 조회합니다.")
	@Test
	void getAllBooths() {
		// given
		Long conferenceId = 1L;
		Pageable pageable = PageRequest.of(0, 10);
		Booth booth = mock(Booth.class);
		Page<Booth> booths = new PageImpl<>(List.of(booth));
		when(boothRepository.findAllByConferenceId(conferenceId, pageable)).thenReturn(booths);

		// when
		Page<BoothResponseDto> response = boothService.getAllBooths(conferenceId, pageable);

		// then
		assertThat(response).isNotNull();
		assertThat(response.getContent().size()).isEqualTo(1);
	}

	@DisplayName("부스를 ID로 조회합니다.")
	@Test
	@WithMockUser(username = "ATTENDEE1", roles = {"ATTENDEE"})
	void getBoothById() {
		// given
		Long conferenceId = 1L;
		Long boothId = 1L;
		Conference conference = mock(Conference.class);

		LocalDate progressDate = LocalDate.of(3012, 11, 11);
		Booth booth = new Booth("부스A", "회사A", "위치A", "101", progressDate, "secretCode", "설명A", conference);

		when(conference.getId()).thenReturn(conferenceId);
		when(boothRepository.findById(boothId)).thenReturn(Optional.of(booth));

		// when
		BoothDetailResponseDto response = boothService.getBoothById("admin1", RoleType.ADMIN, conferenceId, boothId);

		// then
		assertThat(response).isNotNull();
		assertThat(response.companyName()).isEqualTo("부스A");
	}

	@DisplayName("존재하지 않는 부스를 조회하면 예외가 발생합니다.")
	@Test
	void getBoothById_NotFound() {
		// given
		Long conferenceId = 1L;
		Long boothId = 1L;
		when(boothRepository.findById(boothId)).thenReturn(Optional.empty());

		// when
		Throwable thrown = catchThrowable(
			() -> boothService.getBoothById("admin1", RoleType.ADMIN, conferenceId, boothId));

		// then
		assertThat(thrown).isInstanceOf(NotFoundBoothException.class);
	}

	@DisplayName("부스 정보를 업데이트합니다.")
	@Test
	void updateBooth() {
		// given
		Long conferenceId = 1L;
		Long boothId = 1L;
		LocalDate progressDate = LocalDate.of(3012, 11, 11);
		BoothRequestDto request = new BoothRequestDto("부스B", "회사B", progressDate, "위치B", "202", "설명B");
		Conference conference = mock(Conference.class);
		when(conference.getId()).thenReturn(conferenceId);
		Booth booth = new Booth("부스A", "회사A", "위치A", "101", progressDate, "secretCode", "설명A", conference);
		when(boothRepository.findById(boothId)).thenReturn(Optional.of(booth));

		// when
		BoothDetailResponseDto response = boothService.updateBooth(conferenceId, boothId, request, null);

		// then
		assertThat(response).isNotNull();
		assertThat(response.companyName()).isEqualTo("부스B");
	}

	@DisplayName("부스를 삭제합니다.")
	@Test
	void deleteBooth() {
		// given
		Long conferenceId = 1L;
		Long boothId = 1L;
		Booth booth = mock(Booth.class);
		when(booth.getConference()).thenReturn(mock(Conference.class));
		when(booth.getConference().getId()).thenReturn(conferenceId);
		when(boothRepository.findById(boothId)).thenReturn(Optional.of(booth));

		// when
		boothService.deleteBooth(conferenceId, boothId);

		// then
		verify(boothRepository, times(1)).delete(booth);
	}

	@DisplayName("부스가 속한 컨퍼런스 ID가 요청과 다르면 예외가 발생합니다.")
	@Test
	void getBoothById_DifferentConference_ThrowsException() {
		// given
		Long requestedConferenceId = 1L;
		Long actualConferenceId = 2L;
		Long boothId = 1L;
		Conference conference = mock(Conference.class);
		Booth booth = new Booth("부스A", "회사A", "위치A", "101",
			LocalDate.of(3012, 11, 11), "secretCode", "설명A", conference);

		when(boothRepository.findById(boothId)).thenReturn(Optional.of(booth));
		when(conference.getId()).thenReturn(actualConferenceId);

		// when
		Throwable thrown = catchThrowable(
			() -> boothService.getBoothById("admin1", RoleType.ADMIN, requestedConferenceId, boothId));

		// then
		assertThat(thrown).isInstanceOf(NotFoundBoothException.class);
	}

	@DisplayName("참가자 정보가 존재하지만 부스 참여 기록이 없으면 isQRVerify는 false")
	@Test
	void getBoothById_AttendeeNoParticipation_returnsFalseQRVerify() {
		// given
		Long conferenceId = 1L;
		Long boothId = 1L;
		String identifier = "attendee@example.com";
		Conference conference = mock(Conference.class);
		Booth booth = new Booth("부스A", "회사A", "위치A", "101",
			LocalDate.of(3012, 11, 11), "secretCode", "설명A", conference);
		Attendee attendee = mock(Attendee.class);

		when(boothRepository.findById(boothId)).thenReturn(Optional.of(booth));
		when(conference.getId()).thenReturn(conferenceId);
		when(attendee.getId()).thenReturn(123L);
		when(conferenceRepository.findById(conferenceId)).thenReturn(Optional.of(conference));
		when(attendeeRepository.findByEmail(identifier)).thenReturn(Optional.of(attendee));
		when(boothParticipationRepository.findByBoothIdAndAttendeeId(boothId, attendee.getId()))
			.thenReturn(Optional.empty());

		// when
		BoothDetailResponseDto response = boothService.getBoothById(identifier, RoleType.ATTENDEE, conferenceId,
			boothId);

		// then
		assertThat(response).isNotNull();
		assertThat(response.isQRVerify()).isFalse(); // QR 인증 안됨
	}

	@DisplayName("참가자 정보와 부스 참여 기록이 모두 존재하면 isQRVerify는 true")
	@Test
	void getBoothById_AttendeeParticipation_returnsTrueQRVerify() {
		// given
		Long conferenceId = 1L;
		Long boothId = 1L;
		String identifier = "attendee@example.com";
		Conference conference = mock(Conference.class);
		Booth booth = new Booth("부스A", "회사A", "위치A", "101",
			LocalDate.of(3012, 11, 11), "secretCode", "설명A", conference);
		ReflectionTestUtils.setField(booth, "id", boothId);

		Attendee attendee = mock(Attendee.class);

		when(boothRepository.findById(boothId)).thenReturn(Optional.of(booth));
		when(conference.getId()).thenReturn(conferenceId);
		when(attendee.getId()).thenReturn(123L);
		when(attendeeRepository.findByEmail(identifier)).thenReturn(Optional.of(attendee));
		when(boothParticipationRepository.findByBoothIdAndAttendeeId(boothId, attendee.getId()))
			.thenReturn(Optional.of(mock()));

		// when
		BoothDetailResponseDto response = boothService.getBoothById(identifier, RoleType.ATTENDEE, conferenceId,
			boothId);

		// then
		assertThat(response).isNotNull();
		assertThat(response.isQRVerify()).isTrue(); // QR 인증 됨
	}

	@DisplayName("부스 수정 요청 시, 요청한 컨퍼런스 ID와 부스의 컨퍼런스 ID가 다르면 예외가 발생합니다.")
	@Test
	void updateBooth_DifferentConferenceId_ThrowsNotFoundConference() {
		// given
		Long requestConferenceId = 1L;
		Long boothConferenceId = 2L;
		Long boothId = 1L;
		Conference conference = mock(Conference.class);
		when(conference.getId()).thenReturn(boothConferenceId);

		Booth booth = new Booth("부스A", "회사A", "위치A", "101",
			LocalDate.of(3012, 11, 11), "secretCode", "설명A", conference);
		when(boothRepository.findById(boothId)).thenReturn(Optional.of(booth));

		BoothRequestDto requestDto = new BoothRequestDto("부스B", "회사B",
			LocalDate.of(3012, 11, 12), "위치B", "202", "설명B");

		// when
		Throwable thrown = catchThrowable(() ->
			boothService.updateBooth(requestConferenceId, boothId, requestDto, null)
		);

		// then
		assertThat(thrown).isInstanceOf(com.synergy.backend.domain.conference.exception.NotFoundConference.class);
	}

	@DisplayName("부스 삭제 시 요청한 컨퍼런스 ID와 부스의 컨퍼런스 ID가 다르면 NotFoundBoothException 발생")
	@Test
	void deleteBooth_DifferentConferenceId_ThrowsNotFoundBoothException() {
		// given
		Long requestConferenceId = 1L;
		Long actualConferenceId = 2L;
		Long boothId = 1L;

		Conference conference = mock(Conference.class);
		when(conference.getId()).thenReturn(actualConferenceId);

		Booth booth = new Booth("부스A", "회사A", "위치A", "101",
			LocalDate.of(3012, 11, 11), "secretCode", "설명A", conference);
		when(boothRepository.findById(boothId)).thenReturn(Optional.of(booth));

		// when
		Throwable thrown = catchThrowable(() -> boothService.deleteBooth(requestConferenceId, boothId));

		// then
		assertThat(thrown).isInstanceOf(NotFoundBoothException.class);
	}

}
