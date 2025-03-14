package com.synergy.backend.domain.member.service;

import static com.synergy.backend.domain.member.entity.RoleType.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.synergy.backend.domain.member.api.dto.request.SignupAttendeeRequestDto;
import com.synergy.backend.domain.member.api.dto.resposne.SignupAttendeeResponseDto;
import com.synergy.backend.domain.member.api.dto.resposne.TokenResponseDto;
import com.synergy.backend.domain.member.entity.Admin;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.Recruiter;
import com.synergy.backend.domain.member.exception.DuplicateEmailException;
import com.synergy.backend.domain.member.exception.InvalidAuthCodeException;
import com.synergy.backend.domain.member.exception.NotFoundUserException;
import com.synergy.backend.domain.member.exception.UnauthorizedException;
import com.synergy.backend.domain.member.repository.AdminRepository;
import com.synergy.backend.domain.member.repository.AttendeeRepository;
import com.synergy.backend.domain.member.repository.RecruiterRepository;
import com.synergy.backend.global.security.CustomUserDetails;
import com.synergy.backend.global.security.JwtProvider;

@ExtendWith(MockitoExtension.class)
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

	@Test
	@DisplayName("리크루터가 올바른 인증 코드로 로그인하면 JWT 토큰이 발급된다.")
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

	@Test
	@DisplayName("잘못된 인증 코드로 로그인하면 예외가 발생한다.")
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
}
