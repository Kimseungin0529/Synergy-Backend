package com.synergy.backend.global.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.synergy.backend.domain.member.entity.Admin;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.Recruiter;
import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.global.jwt.JwtProperties;
import com.synergy.backend.global.jwt.JwtProvider;

@ExtendWith(MockitoExtension.class)
class JwtProviderTest {
	@Mock
	private JwtProperties jwtProperties;

	private JwtProvider jwtProvider;

	@BeforeEach
	void setUp() {
		when(jwtProperties.secret()).thenReturn("your-secret-key-which-is-at-least-32-bytes-long!!!");
		lenient().when(jwtProperties.accessTokenExpiration()).thenReturn(Duration.ofMinutes(30));
		lenient().when(jwtProperties.refreshTokenExpiration()).thenReturn(Duration.ofDays(30));
		jwtProvider = new JwtProvider(jwtProperties);
	}

	@Test
	@DisplayName("Attendee 사용자의 JWT 토큰이 정상적으로 생성된다.")
	void generateAccessToken_Attendee_Success() {
		// Given
		Attendee attendee = Attendee.of("attendee@example.com", "hashedPassword", "user", "phone");
		CustomUserDetails userDetails = new CustomUserDetails(attendee);

		// When
		String token = jwtProvider.generateAccessToken(userDetails);

		// Then
		assertNotNull(token);
		assertEquals(attendee.getEmail(), jwtProvider.getIdentifierFromToken(token));
		assertEquals(RoleType.ATTENDEE, jwtProvider.getRoleTypeFromToken(token));
	}

	@Test
	@DisplayName("Admin 사용자의 JWT 토큰이 정상적으로 생성된다.")
	void generateAccessToken_Admin_Success() {
		// Given
		Admin admin = Admin.of("ADMINAUTHCODE123");
		CustomUserDetails userDetails = new CustomUserDetails(admin);

		// When
		String token = jwtProvider.generateAccessToken(userDetails);

		// Then
		assertNotNull(token);
		assertEquals(admin.getAdminAuthCode(), jwtProvider.getIdentifierFromToken(token));
		assertEquals(RoleType.ADMIN, jwtProvider.getRoleTypeFromToken(token));
	}

	@Test
	@DisplayName("Recruiter 사용자의 JWT 토큰이 정상적으로 생성된다.")
	void generateAccessToken_Recruiter_Success() {
		// Given
		Recruiter recruiter = Recruiter.of("RECRUITERAUTHCODE123");
		CustomUserDetails userDetails = new CustomUserDetails(recruiter);

		// When
		String token = jwtProvider.generateAccessToken(userDetails);

		// Then
		assertNotNull(token);
		assertEquals(recruiter.getRecruiterAuthCode(), jwtProvider.getIdentifierFromToken(token));
		assertEquals(RoleType.RECRUITER, jwtProvider.getRoleTypeFromToken(token));
	}

	@Test
	@DisplayName("잘못된 JWT는 검증에 실패한다.")
	void validateToken_InvalidToken_ReturnsFalse() {
		// Given
		String invalidToken = "this.is.a.fake.token";

		// When
		boolean isValid = jwtProvider.validateToken(invalidToken);

		// Then
		assertFalse(isValid);
	}
}
