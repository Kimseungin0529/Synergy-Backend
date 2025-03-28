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

import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.conference.exception.InvalidTicketCodeException;
import com.synergy.backend.domain.conference.repository.ConferenceRepository;
import com.synergy.backend.domain.member.api.dto.request.SignupAttendeeRequestDto;
import com.synergy.backend.domain.member.api.dto.resposne.SignupAttendeeResponseDto;
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
import com.synergy.backend.domain.member.vo.TokenWithRefreshToken;
import com.synergy.backend.domain.point.service.PointService;
import com.synergy.backend.global.jwt.JwtProvider;
import com.synergy.backend.global.mail.MailService;
import com.synergy.backend.global.mail.exception.EmailNotVerifiedException;
import com.synergy.backend.global.security.CustomUserDetails;
import com.synergy.backend.global.security.CustomUserDetailsService;
import com.synergy.backend.global.token.TokenService;
import com.synergy.backend.global.token.exception.InvalidRefreshTokenException;

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
	private ConferenceRepository conferenceRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private JwtProvider jwtProvider;

	@Mock
	private PointService pointService;

	@Mock
	private MailService mailService;

	@Mock
	private TokenService tokenService;

	@Mock
	private CustomUserDetailsService userDetailsService;

	private SignupAttendeeRequestDto requestDto;
	private Attendee mockAttendee;

	@BeforeEach
	void setUp() {
		requestDto = new SignupAttendeeRequestDto(
			"UserA",
			"UserA@example.com",
			"abc123",
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
		when(conferenceRepository.findByTicketCode(anyString())).thenReturn(Optional.of(mock(Conference.class)));

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
		when(jwtProvider.generateAccessToken(any(CustomUserDetails.class))).thenReturn(("token"));

		// When
		TokenWithRefreshToken response = authService.loginAsAttendee(requestDto.email(), requestDto.password());

		// Then
		assertNotNull(response);
		assertEquals("token", response.tokenResponseDto().accessToken());
		assertEquals("UserA@example.com", response.tokenResponseDto().identifier());
		assertEquals(ATTENDEE, response.tokenResponseDto().role());
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
		when(jwtProvider.generateAccessToken(any(CustomUserDetails.class))).thenReturn(expectedToken);

		// When
		TokenWithRefreshToken response = authService.loginAsAdminOrRecruiter(authCode);

		// Then
		assertNotNull(response);
		assertEquals("mocked-admin-token", response.tokenResponseDto().accessToken());
		assertEquals(ADMIN, response.tokenResponseDto().role());

		verify(adminRepository).findByAdminAuthCode(authCode);
		verify(jwtProvider).generateAccessToken(any(CustomUserDetails.class));

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
		when(jwtProvider.generateAccessToken(any(CustomUserDetails.class))).thenReturn(expectedToken);

		// When
		TokenWithRefreshToken response = authService.loginAsAdminOrRecruiter(authCode);

		// Then
		assertNotNull(response);
		assertEquals("mocked-recruiter-token", response.tokenResponseDto().accessToken());
		assertEquals(RECRUITER, response.tokenResponseDto().role());

		verify(adminRepository).findByAdminAuthCode(authCode);
		verify(recruiterRepository).findByRecruiterAuthCode(authCode);
		verify(jwtProvider).generateAccessToken(any(CustomUserDetails.class));
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

		verify(jwtProvider, never()).generateAccessToken(any(CustomUserDetails.class));
	}

	@DisplayName("회원가입 성공 시 회원가입 포인트가 적립된다.")
	@Test
	void registerEarnPoint() {
		// given
		when(attendeeRepository.findByEmail(requestDto.email())).thenReturn(Optional.empty());
		when(passwordEncoder.encode(requestDto.password())).thenReturn("encodedPassword");
		when(mailService.isVerified(anyString())).thenReturn(true);
		when(conferenceRepository.findByTicketCode(anyString())).thenReturn(Optional.of(mock(Conference.class)));
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

	@DisplayName("사용자 로그인 시 엑세스토큰과 리프레시토큰이 발급된다.")
	@Test
	void loginAsAttendee_TokenIssued() {
		// given
		String accessToken = "mockAccessToken";
		String refreshToken = "mockRefreshToken";

		when(attendeeRepository.findByEmail(requestDto.email())).thenReturn(Optional.of(mockAttendee));
		when(passwordEncoder.matches(requestDto.password(), mockAttendee.getPassword())).thenReturn(true);
		when(jwtProvider.generateAccessToken(any(CustomUserDetails.class))).thenReturn(accessToken);
		when(jwtProvider.generateRefreshToken(any(CustomUserDetails.class))).thenReturn(refreshToken);

		// when
		TokenWithRefreshToken result = authService.loginAsAttendee(requestDto.email(), requestDto.password());

		// then
		assertThat(result.tokenResponseDto().accessToken()).isEqualTo(accessToken);
		assertThat(result.refreshToken()).isEqualTo(refreshToken);
		verify(tokenService).storeRefreshToken(requestDto.email(), refreshToken);
	}

	@DisplayName("유효한 리프레시 토큰으로 액세스 토큰과 새로운 리프레시 토큰을 재발급한다.")
	@Test
	void reissueRefreshToken_success() {
		// given
		String currentRefreshToken = "validRefreshToken";
		String newAccessToken = "newAccessToken";
		String newRefreshToken = "newRefreshToken";
		String identifier = mockAttendee.getIdentifier();
		CustomUserDetails userDetails = new CustomUserDetails(mockAttendee);

		when(jwtProvider.validateRefreshToken(currentRefreshToken)).thenReturn(true);
		when(jwtProvider.getIdentifierFromToken(currentRefreshToken)).thenReturn(identifier);
		when(tokenService.getStoredRefreshToken(identifier)).thenReturn(currentRefreshToken);
		when(userDetailsService.loadUserByUsername(identifier)).thenReturn(userDetails);
		when(jwtProvider.generateAccessToken(userDetails)).thenReturn(newAccessToken);
		when(jwtProvider.generateRefreshToken(userDetails)).thenReturn(newRefreshToken);

		// when
		TokenWithRefreshToken result = authService.reissueRefreshToken(currentRefreshToken);

		// then
		assertThat(result).isNotNull();
		assertThat(result.refreshToken()).isEqualTo(newRefreshToken);
		assertThat(result.tokenResponseDto().accessToken()).isEqualTo(newAccessToken);
		assertThat(result.tokenResponseDto().identifier()).isEqualTo(identifier);
		assertThat(result.tokenResponseDto().role()).isEqualTo(mockAttendee.getRole());

		verify(tokenService).storeRefreshToken(identifier, newRefreshToken);
	}

	@DisplayName("리프레시 토큰이 유효하지 않으면 예외를 던진다.")
	@Test
	void reissueRefreshToken_invalidToken() {
		// given
		String invalidToken = "invalidToken";
		when(jwtProvider.validateRefreshToken(invalidToken)).thenReturn(false);

		// then
		assertThatThrownBy(() -> authService.reissueRefreshToken(invalidToken))
			.isInstanceOf(InvalidRefreshTokenException.class);
	}

	@DisplayName("저장된 리프레시 토큰과 다르면 예외를 던진다.")
	@Test
	void reissueRefreshToken_tokenMismatch() {
		// given
		String currentRefreshToken = "current";
		String identifier = "user@example.com";

		when(jwtProvider.validateRefreshToken(currentRefreshToken)).thenReturn(true);
		when(jwtProvider.getIdentifierFromToken(currentRefreshToken)).thenReturn(identifier);
		when(tokenService.getStoredRefreshToken(identifier)).thenReturn("different");

		// then
		assertThatThrownBy(() -> authService.reissueRefreshToken(currentRefreshToken))
			.isInstanceOf(InvalidRefreshTokenException.class);
	}

	@DisplayName("로그아웃 시 저장된 리프레시 토큰을 삭제한다.")
	@Test
	void logout_success() {
		// given
		String refreshToken = "validRefreshToken";
		String identifier = mockAttendee.getIdentifier();

		when(jwtProvider.getIdentifierFromToken(refreshToken)).thenReturn(identifier);

		// when
		authService.logout(refreshToken);

		// then
		verify(tokenService).deleteRefreshToken(identifier);
	}

	@DisplayName("재설정 요청 시 존재하지 않는 이메일이면 예외 발생")
	@Test
	void passwordResetRequest_emailNotFound_ThrowsException() {
		// given
		when(mailService.isVerified(requestDto.email())).thenReturn(true);
		when(attendeeRepository.findByEmail(requestDto.email())).thenReturn(Optional.empty());

		// when&then
		assertThatThrownBy(() -> authService.passwordResetRequest(
			requestDto.email(), requestDto.name(), requestDto.phone()))
			.isInstanceOf(InvalidAccountInformationException.class);
	}

	@DisplayName("비밀번호 재설정 요청 시 이메일 인증이 안 되어 있으면 예외가 발생한다.")
	@Test
	void passwordResetRequest_emailNotVerified_ThrowsException() {
		// given
		when(mailService.isVerified(requestDto.email())).thenReturn(false);

		// when & then
		assertThatThrownBy(() -> authService.passwordResetRequest(
			requestDto.email(), requestDto.name(), requestDto.phone()))
			.isInstanceOf(EmailNotVerifiedException.class);
	}

	@DisplayName("티켓 코드가 유효하지 않으면 예외가 발생한다.")
	@Test
	void registerAttendee_InvalidTicketCode_ThrowsException() {
		// given
		when(attendeeRepository.findByEmail(requestDto.email())).thenReturn(Optional.empty());
		when(mailService.isVerified(requestDto.email())).thenReturn(true);
		when(conferenceRepository.findByTicketCode(requestDto.ticketCode())).thenReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> authService.registerAttendee(requestDto))
			.isInstanceOf(InvalidTicketCodeException.class);
	}

	@DisplayName("리프레시 토큰 재발급 시 사용자 정보가 존재하지 않으면 예외 발생")
	@Test
	void reissueRefreshToken_UserNotFound_ThrowsException() {
		// given
		String token = "validToken";
		String identifier = "someone@example.com";

		when(jwtProvider.validateRefreshToken(token)).thenReturn(true);
		when(jwtProvider.getIdentifierFromToken(token)).thenReturn(identifier);
		when(tokenService.getStoredRefreshToken(identifier)).thenReturn(token);
		when(userDetailsService.loadUserByUsername(identifier)).thenThrow(new NotFoundUserException());

		// when & then
		assertThatThrownBy(() -> authService.reissueRefreshToken(token))
			.isInstanceOf(NotFoundUserException.class);
	}
}
