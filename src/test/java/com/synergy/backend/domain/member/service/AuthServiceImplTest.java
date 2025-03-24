package com.synergy.backend.domain.member.service;

import static com.synergy.backend.domain.member.entity.RoleType.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import com.synergy.backend.domain.member.api.dto.request.SignupAttendeeRequestDto;
import com.synergy.backend.domain.member.api.dto.resposne.SignupAttendeeResponseDto;
import com.synergy.backend.domain.member.api.dto.resposne.TokenResponseDto;
import com.synergy.backend.domain.member.entity.Admin;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.Recruiter;
import com.synergy.backend.domain.member.exception.DuplicateEmailException;
import com.synergy.backend.domain.member.exception.InvalidAccountInformationException;
import com.synergy.backend.domain.member.exception.InvalidAuthCodeException;
import com.synergy.backend.domain.member.exception.NotFoundUserException;
import com.synergy.backend.domain.member.exception.SameAsPreviousPasswordException;
import com.synergy.backend.domain.member.exception.UnauthorizedException;
import com.synergy.backend.domain.member.repository.AdminRepository;
import com.synergy.backend.domain.member.repository.AttendeeRepository;
import com.synergy.backend.domain.member.repository.RecruiterRepository;
import com.synergy.backend.domain.point.service.PointService;
import com.synergy.backend.global.mail.MailService;
import com.synergy.backend.global.mail.exception.EmailNotVerifiedException;
import com.synergy.backend.global.security.CustomUserDetails;
import com.synergy.backend.global.security.JwtProvider;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@Slf4j
class AuthServiceImplTest {

	@InjectMocks
	private AuthServiceImpl authService;

	@Mock
	private AttendeeRepository attendeeRepository;

	@Mock
	private AdminRepository adminRepository;

	@Mock
	private RecruiterRepository recruiterRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private JwtProvider jwtProvider;

	@Mock
	private PointService pointService;

	@Mock
	private MailService mailService;

	private SignupAttendeeRequestDto requestDto;
	private Attendee mockAttendee;

	@BeforeEach
	void setUp() {
		requestDto = new SignupAttendeeRequestDto(
			"UserA",
			"UserA@example.com",
			"securepassword",
			"01012345678"
		);

		mockAttendee = Attendee.of(
			requestDto.email(),
			"encodedPassword",
			requestDto.name(),
			requestDto.phone()
		);
	}

	@DisplayName("참가자가 회원가입을 하면 새로운 회원이 정상적으로 저장된다.")
	@Test
	void registerAttendee_Success() {
		// Given
		when(attendeeRepository.findByEmail(requestDto.email())).thenReturn(Optional.empty());
		when(passwordEncoder.encode(requestDto.password())).thenReturn("encodedPassword");
		when(attendeeRepository.save(any(Attendee.class))).thenReturn(mockAttendee);
		when(mailService.isVerified(anyString())).thenReturn(true);

		// When
		SignupAttendeeResponseDto response = authService.registerAttendee(requestDto);

		// Then
		assertNotNull(response);
		assertEquals(requestDto.email(), response.email());
		assertEquals(requestDto.name(), response.name());
		verify(attendeeRepository).save(any(Attendee.class));
	}

	@DisplayName("이미 존재하는 이메일로 회원가입을 시도하면 예외가 발생한다.")
	@Test
	void registerAttendee_DuplicateEmail_ThrowsException() {
		// Given
		when(attendeeRepository.findByEmail(requestDto.email())).thenReturn(Optional.of(mockAttendee));
		when(mailService.isVerified(anyString())).thenReturn(true);

		// When & Then
		assertThrows(DuplicateEmailException.class, () -> authService.registerAttendee(requestDto));
	}

	@DisplayName("참가자 로그인 시 이메일과 비밀번호가 일치하면 JWT 토큰이 정상적으로 생성된다.")
	@Test
	void loginAsAttendee_Success() {
		// Given
		when(attendeeRepository.findByEmail(requestDto.email())).thenReturn(Optional.of(mockAttendee));
		when(passwordEncoder.matches(requestDto.password(), mockAttendee.getPassword())).thenReturn(true);
		when(jwtProvider.generateToken(any(CustomUserDetails.class))).thenReturn(("token"));

		// When
		TokenResponseDto response = authService.loginAsAttendee(requestDto.email(), requestDto.password());

		// Then
		assertNotNull(response);
		assertEquals("token", response.accessToken());
		assertEquals("UserA@example.com", response.identifier());
		assertEquals(ATTENDEE.toString(), response.role());
	}

	@DisplayName("참가자 로그인 시 이메일이 존재하지 않으면 예외가 발생한다.")
	@Test
	void loginAsAttendee_UserNotFound_ThrowsException() {
		// Given
		when(attendeeRepository.findByEmail(requestDto.email())).thenReturn(Optional.empty());

		// When & Then
		assertThrows(NotFoundUserException.class,
			() -> authService.loginAsAttendee(requestDto.email(), requestDto.password()));
	}

	@DisplayName("참가자 로그인 시 비밀번호가 일치하지 않으면 예외가 발생한다.")
	@Test
	void loginAsAttendee_InvalidPassword_ThrowsException() {
		// Given
		when(attendeeRepository.findByEmail(requestDto.email())).thenReturn(Optional.of(mockAttendee));
		when(passwordEncoder.matches(requestDto.password(), mockAttendee.getPassword())).thenReturn(false);

		// When & Then
		assertThrows(UnauthorizedException.class,
			() -> authService.loginAsAttendee(requestDto.email(), requestDto.password()));
	}

	@DisplayName("관리자가 올바른 인증 코드로 로그인하면 JWT 토큰이 발급된다.")
	@Test
	void loginAsAdmin_Success() {
		// Given
		String authCode = "validAdminAuthCode";
		Admin mockAdmin = Admin.of(authCode);
		String expectedToken = "mocked-admin-token";

		when(adminRepository.findByAdminAuthCode(authCode)).thenReturn(Optional.of(mockAdmin));
		when(jwtProvider.generateToken(any(CustomUserDetails.class))).thenReturn(expectedToken);

		// When
		TokenResponseDto response = authService.loginAsAdminOrRecruiter(authCode);

		// Then
		assertNotNull(response);
		assertEquals("mocked-admin-token", response.accessToken());
		assertEquals(ADMIN.toString(), response.role());

		verify(adminRepository).findByAdminAuthCode(authCode);
		verify(jwtProvider).generateToken(any(CustomUserDetails.class));

		verify(recruiterRepository, never()).findByRecruiterAuthCode(anyString());
	}

	@DisplayName("리크루터가 올바른 인증 코드로 로그인하면 JWT 토큰이 발급된다.")
	@Test
	void loginAsRecruiter_Success() {
		// Given
		String authCode = "validRecruiterAuthCode";
		Recruiter mockRecruiter = Recruiter.of(authCode);

		String expectedToken = "mocked-recruiter-token";

		when(adminRepository.findByAdminAuthCode(authCode)).thenReturn(Optional.empty()); // 관리자로 찾으면 없음
		when(recruiterRepository.findByRecruiterAuthCode(authCode)).thenReturn(Optional.of(mockRecruiter));
		when(jwtProvider.generateToken(any(CustomUserDetails.class))).thenReturn(expectedToken);

		// When
		TokenResponseDto response = authService.loginAsAdminOrRecruiter(authCode);

		// Then
		assertNotNull(response);
		assertEquals("mocked-recruiter-token", response.accessToken());
		assertEquals(RECRUITER.toString(), response.role());

		verify(adminRepository).findByAdminAuthCode(authCode);
		verify(recruiterRepository).findByRecruiterAuthCode(authCode);
		verify(jwtProvider).generateToken(any(CustomUserDetails.class));
	}

	@DisplayName("관리자나 채용담당자가 잘못된 인증 코드로 로그인하면 예외가 발생한다.")
	@Test
	void loginAsAdminOrRecruiter_InvalidAuthCode_ThrowsException() {
		// Given
		String invalidAuthCode = "invalidAuthCode";

		when(adminRepository.findByAdminAuthCode(invalidAuthCode)).thenReturn(Optional.empty());
		when(recruiterRepository.findByRecruiterAuthCode(invalidAuthCode)).thenReturn(Optional.empty());

		// When & Then
		assertThrows(InvalidAuthCodeException.class, () -> authService.loginAsAdminOrRecruiter(invalidAuthCode));

		verify(adminRepository).findByAdminAuthCode(invalidAuthCode);
		verify(recruiterRepository).findByRecruiterAuthCode(invalidAuthCode);

		verify(jwtProvider, never()).generateToken(any(CustomUserDetails.class));
	}

	@DisplayName("회원가입 성공 시 회원가입 포인트가 적립된다.")
	@Test
	void registerEarnPoint() {
		// given
		when(attendeeRepository.findByEmail(requestDto.email())).thenReturn(Optional.empty());
		when(passwordEncoder.encode(requestDto.password())).thenReturn("encodedPassword");
		when(mailService.isVerified(anyString())).thenReturn(true);
		when(attendeeRepository.save(any(Attendee.class)))
			.thenAnswer(invocation -> {
				Attendee attendee = invocation.getArgument(0);
				ReflectionTestUtils.setField(attendee, "id", 1L);
				return attendee;
			});

		// when
		SignupAttendeeResponseDto response = authService.registerAttendee(requestDto);

		// then
		assertThat(response.email()).isEqualTo("UserA@example.com");
		ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
		verify(pointService).addSignupPoint(captor.capture());
		assertThat(captor.getValue()).isEqualTo(1L);
	}

	@DisplayName("이메일 인증이 완료되지 않은 경우 회원가입 시 예외가 발생한다.")
	@Test
	void registerAttendee_EmailNotVerified_ThrowsException() {
		// given
		when(mailService.isVerified(requestDto.email())).thenReturn(false);

		// when & then
		assertThatThrownBy(() -> authService.registerAttendee(requestDto))
			.isInstanceOf(EmailNotVerifiedException.class);
	}

	@DisplayName("비밀번호 재설정 요청 시 이름 또는 전화번호가 일치하지 않으면 예외가 발생한다.")
	@Test
	void passwordResetRequest_InvalidAccountInformation_ThrowsException() {
		// Given
		when(mailService.isVerified(requestDto.email())).thenReturn(true);
		when(attendeeRepository.findByEmail(requestDto.email())).thenReturn(Optional.of(mockAttendee));

		// 이름 또는 전화번호가 다르게 입력됨
		String wrongName = "WrongName";
		String wrongPhone = "01000000000";

		// When & Then
		assertThatThrownBy(() -> authService.passwordResetRequest(requestDto.email(), wrongName, wrongPhone))
			.isInstanceOf(InvalidAccountInformationException.class);
	}

	@DisplayName("비밀번호 재설정 시 기존 비밀번호와 동일한 경우 예외가 발생한다.")
	@Test
	void passwordReset_SameAsPreviousPassword_ThrowsException() {
		// Given
		when(attendeeRepository.findByEmail(requestDto.email())).thenReturn(Optional.of(mockAttendee));
		when(passwordEncoder.matches("samePassword", mockAttendee.getPassword())).thenReturn(true);

		// When & Then
		assertThatThrownBy(() -> authService.passwordReset(requestDto.email(), "samePassword"))
			.isInstanceOf(SameAsPreviousPasswordException.class);
	}

	@DisplayName("비밀번호 재설정이 성공적으로 이루어진다.")
	@Test
	void passwordReset_Success() {
		// Given
		when(attendeeRepository.findByEmail(requestDto.email())).thenReturn(Optional.of(mockAttendee));
		when(passwordEncoder.matches("newPassword", mockAttendee.getPassword())).thenReturn(false);
		when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

		// When
		authService.passwordReset(requestDto.email(), "newPassword");

		// Then
		assertThat(mockAttendee.getPassword()).isEqualTo("encodedNewPassword");
	}
}
