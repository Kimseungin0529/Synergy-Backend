package com.synergy.backend.global.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.util.Date;

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
import com.synergy.backend.global.token.exception.InvalidAccessTokenException;
import com.synergy.backend.global.token.exception.InvalidRefreshTokenException;
import com.synergy.backend.global.token.exception.MissingRoleClaimException;
import com.synergy.backend.global.token.exception.InvalidRoleClaimException;
import com.synergy.backend.global.token.exception.ExpiredRefreshTokenException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

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
	@DisplayName("AccessToken이 유효하면 true를 반환한다.")
	void validateAccessToken_ValidToken_ReturnsTrue() {
		// given
		Attendee attendee = Attendee.of("attendee@example.com", "hashedPassword", "user", "phone");
		CustomUserDetails userDetails = new CustomUserDetails(attendee);
		String accessToken = jwtProvider.generateAccessToken(userDetails);

		// when
		boolean isValid = jwtProvider.validateAccessToken(accessToken);

		// then
		assertTrue(isValid);
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
	@DisplayName("잘못된 JWT는 InvalidAccessTokenException 예외를 발생시킨다.")
	void validateToken_InvalidAccessToken_ThrowsException() {
		// Given
		String invalidToken = "this.is.a.fake.token";

		// When & Then
		assertThrows(InvalidAccessTokenException.class, () -> jwtProvider.validateAccessToken(invalidToken));
	}

	@Test
	@DisplayName("만료된 JWT는 ExpiredJwtException을 발생시킨다.")
	void validateToken_ExpiredAccessToken_ThrowsException() {
		// Given
		Attendee attendee = Attendee.of("attendee@example.com", "hashedPassword", "user", "phone");
		CustomUserDetails userDetails = new CustomUserDetails(attendee);
		String expiredToken = Jwts.builder()
			.setSubject(userDetails.getIdentifier())
			.claim("id", userDetails.getId())
			.claim("role", userDetails.getRole().getAuthority())
			.claim("type", "access")
			.setIssuedAt(new Date(System.currentTimeMillis() - 60000))
			.setExpiration(new Date(System.currentTimeMillis() - 1000)) // already expired
			.signWith(Keys.hmacShaKeyFor("your-secret-key-which-is-at-least-32-bytes-long!!!".getBytes()),
				SignatureAlgorithm.HS256)
			.compact();

		// When & Then
		assertThrows(io.jsonwebtoken.ExpiredJwtException.class, () -> jwtProvider.validateAccessToken(expiredToken));
	}

	@Test
	@DisplayName("RefreshToken을 AccessToken으로 사용 시 InvalidAccessTokenException 발생")
	void validateAccessToken_UsingRefreshAsAccess_ThrowsException() {
		// Given
		Attendee attendee = Attendee.of("attendee@example.com", "hashedPassword", "user", "phone");
		CustomUserDetails userDetails = new CustomUserDetails(attendee);
		String refreshToken = Jwts.builder()
			.setSubject(userDetails.getIdentifier())
			.claim("id", userDetails.getId())
			.claim("role", userDetails.getRole().getAuthority())
			.claim("type", "refresh")
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + 100000)) // valid time
			.signWith(Keys.hmacShaKeyFor("your-secret-key-which-is-at-least-32-bytes-long!!!".getBytes()),
				SignatureAlgorithm.HS256)
			.compact();

		// When & Then
		assertThrows(InvalidAccessTokenException.class, () -> jwtProvider.validateAccessToken(refreshToken));
	}

	@Test
	@DisplayName("RefreshToken이 유효하면 true를 반환한다.")
	void validateRefreshToken_ValidToken_ReturnsTrue() {
		// given
		Attendee attendee = Attendee.of("attendee@example.com", "hashedPassword", "user", "phone");
		CustomUserDetails userDetails = new CustomUserDetails(attendee);
		String refreshToken = jwtProvider.generateRefreshToken(userDetails);

		// when
		boolean isValid = jwtProvider.validateRefreshToken(refreshToken);

		// then
		assertTrue(isValid);
	}

	@Test
	@DisplayName("AccessToken으로 RefreshToken 검증 시 InvalidRefreshTokenException 발생")
	void validateRefreshToken_UsingAccessToken_ThrowsException() {
		// given
		Attendee attendee = Attendee.of("attendee@example.com", "hashedPassword", "user", "phone");
		CustomUserDetails userDetails = new CustomUserDetails(attendee);
		String accessToken = jwtProvider.generateAccessToken(userDetails);

		// when & then
		assertThrows(InvalidRefreshTokenException.class, () -> jwtProvider.validateRefreshToken(accessToken));
	}

	@Test
	@DisplayName("유효하지 않은 RefreshToken은 InvalidRefreshTokenException을 발생시킨다.")
	void validateRefreshToken_InvalidToken_ThrowsException() {
		// given
		String invalidToken = "invalid.refresh.token";

		// when & then
		assertThrows(InvalidRefreshTokenException.class, () -> jwtProvider.validateRefreshToken(invalidToken));
	}

	@Test
	@DisplayName("만료된 RefreshToken은 ExpiredRefreshTokenException을 발생시킨다.")
	void validateRefreshToken_Expired_ThrowsException() {
		// given
		Attendee attendee = Attendee.of("attendee@example.com", "hashedPassword", "user", "phone");
		CustomUserDetails userDetails = new CustomUserDetails(attendee);
		String expiredToken = Jwts.builder()
			.setSubject(userDetails.getIdentifier())
			.claim("id", userDetails.getId())
			.claim("role", userDetails.getRole().getAuthority())
			.claim("type", "refresh")
			.setIssuedAt(new Date(System.currentTimeMillis() - 60000))
			.setExpiration(new Date(System.currentTimeMillis() - 1000))
			.signWith(Keys.hmacShaKeyFor("your-secret-key-which-is-at-least-32-bytes-long!!!".getBytes()), SignatureAlgorithm.HS256)
			.compact();

		// when & then
		assertThrows(ExpiredRefreshTokenException.class, () -> jwtProvider.validateRefreshToken(expiredToken));
	}

	@Test
	@DisplayName("토큰에서 id를 추출할 수 있다.")
	void getIdFromToken_ReturnsId() {
		// given
		Attendee attendee = Attendee.of("attendee@example.com", "hashedPassword", "user", "phone");
		CustomUserDetails userDetails = new CustomUserDetails(attendee);
		String token = jwtProvider.generateAccessToken(userDetails);

		// when
		Long id = jwtProvider.getIdFromToken(token);

		// then
		assertEquals(userDetails.getId(), id);
	}

	@Test
	@DisplayName("role claim이 없으면 MissingRoleClaimException이 발생한다.")
	void getRoleTypeFromToken_MissingRole_ThrowsException() {
		// given
		String token = Jwts.builder()
			.setSubject("subject")
			.claim("id", 1L)
			.claim("type", "access")
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + 60000))
			.signWith(Keys.hmacShaKeyFor("your-secret-key-which-is-at-least-32-bytes-long!!!".getBytes()), SignatureAlgorithm.HS256)
			.compact();

		// when & then
		assertThrows(MissingRoleClaimException.class, () -> jwtProvider.getRoleTypeFromToken(token));
	}

	@Test
	@DisplayName("잘못된 role claim이면 InvalidRoleClaimException이 발생한다.")
	void getRoleTypeFromToken_InvalidRole_ThrowsException() {
		// given
		String token = Jwts.builder()
			.setSubject("subject")
			.claim("id", 1L)
			.claim("role", "INVALID_ROLE")
			.claim("type", "access")
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + 60000))
			.signWith(Keys.hmacShaKeyFor("your-secret-key-which-is-at-least-32-bytes-long!!!".getBytes()), SignatureAlgorithm.HS256)
			.compact();

		// when & then
		assertThrows(InvalidRoleClaimException.class, () -> jwtProvider.getRoleTypeFromToken(token));
	}
}
