package com.synergy.backend.global.security;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Base64;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.synergy.backend.domain.member.entity.Admin;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.Recruiter;
import com.synergy.backend.domain.member.entity.RoleType;

class JwtProviderTest {

	private final String TEST_SECRET_KEY = Base64.getEncoder()
		.encodeToString("SuperSecureKeyForJWTAuthWhichIsLongEnough123!".getBytes());

	private JwtProvider jwtProvider;

	@BeforeEach
	void setUp() {
		jwtProvider = new JwtProvider(TEST_SECRET_KEY);
	}

	@Test
	@DisplayName("Attendee 사용자의 JWT 토큰이 정상적으로 생성된다.")
	void generateToken_Attendee_Success() {
		// Given
		Attendee attendee = Attendee.of("attendee@example.com", "hashedPassword", "user", "phone");
		CustomUserDetails userDetails = new CustomUserDetails(attendee);

		// When
		String token = jwtProvider.generateToken(userDetails);

		// Then
		assertNotNull(token);
		assertEquals(attendee.getEmail(), jwtProvider.getEmailOrAuthCodeFromToken(token));
		assertEquals(RoleType.ATTENDEE, jwtProvider.getRoleTypeFromToken(token));
	}

	@Test
	@DisplayName("Admin 사용자의 JWT 토큰이 정상적으로 생성된다.")
	void generateToken_Admin_Success() {
		// Given
		Admin admin = Admin.of("ADMINAUTHCODE123");
		CustomUserDetails userDetails = new CustomUserDetails(admin);

		// When
		String token = jwtProvider.generateToken(userDetails);

		// Then
		assertNotNull(token);
		assertEquals(admin.getAdminAuthCode(), jwtProvider.getEmailOrAuthCodeFromToken(token));
		assertEquals(RoleType.ADMIN, jwtProvider.getRoleTypeFromToken(token));
	}

	@Test
	@DisplayName("Recruiter 사용자의 JWT 토큰이 정상적으로 생성된다.")
	void generateToken_Recruiter_Success() {
		// Given
		Recruiter recruiter = Recruiter.of("RECRUITERAUTHCODE123");
		CustomUserDetails userDetails = new CustomUserDetails(recruiter);

		// When
		String token = jwtProvider.generateToken(userDetails);

		// Then
		assertNotNull(token);
		assertEquals(recruiter.getRecruiterAuthCode(), jwtProvider.getEmailOrAuthCodeFromToken(token));
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
