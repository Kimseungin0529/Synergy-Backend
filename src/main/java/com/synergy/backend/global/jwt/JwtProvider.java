package com.synergy.backend.global.jwt;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.global.security.CustomUserDetails;
import com.synergy.backend.global.token.exception.ExpiredRefreshTokenException;
import com.synergy.backend.global.token.exception.InvalidAccessTokenException;
import com.synergy.backend.global.token.exception.InvalidRefreshTokenException;
import com.synergy.backend.global.token.exception.InvalidRoleClaimException;
import com.synergy.backend.global.token.exception.MissingRoleClaimException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtProvider {

	private final JwtProperties jwtProperties;
	private final SecretKey key;

	public JwtProvider(JwtProperties jwtProperties) {
		this.jwtProperties = jwtProperties;
		this.key = Keys.hmacShaKeyFor(jwtProperties.secret().getBytes(StandardCharsets.UTF_8));
	}

	public String generateAccessToken(CustomUserDetails userDetails) {
		return generateToken(userDetails, jwtProperties.accessTokenExpiration(), "access");
	}

	public String generateRefreshToken(CustomUserDetails userDetails) {
		return generateToken(userDetails, jwtProperties.refreshTokenExpiration(), "refresh");
	}

	public boolean validateAccessToken(String token) {
		try {
			Claims claims = Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();

			String type = claims.get("type", String.class);
			if (!"access".equals(type)) {
				throw new InvalidAccessTokenException();
			}
			return true;
		} catch (ExpiredJwtException e) {
			throw e;
		} catch (JwtException | IllegalArgumentException e) {
			throw new InvalidAccessTokenException();
		}
	}

	public boolean validateRefreshToken(String token) {
		try {
			Claims claims = Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();

			String type = claims.get("type", String.class);
			if (!"refresh".equals(type)) {
				throw new InvalidRefreshTokenException();
			}
			return true;
		} catch (ExpiredJwtException e) {
			throw new ExpiredRefreshTokenException();
		} catch (JwtException | IllegalArgumentException e) {
			log.warn("[validateRefreshToken] JwtException caught: {}", e.getClass().getSimpleName());
			throw new InvalidRefreshTokenException();
		}
	}

	public String getIdentifierFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
	}

	public Long getIdFromToken(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody()
			.get("id", Long.class);
	}

	public RoleType getRoleTypeFromToken(String token) {
		String role = Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody()
			.get("role", String.class);

		if (role == null) {
			throw new MissingRoleClaimException();
		}

		if (role.startsWith("ROLE_")) {
			role = role.substring(5);
		}

		try {
			return RoleType.valueOf(role);
		} catch (IllegalArgumentException e) {
			throw new InvalidRoleClaimException();
		}
	}

	private String generateToken(CustomUserDetails userDetails, Duration expiration, String tokenType) {
		return Jwts.builder()
			.setSubject(userDetails.getIdentifier())
			.claim("id", userDetails.getId())
			.claim("role", userDetails.getRole().getAuthority())
			.claim("type", tokenType)
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + expiration.toMillis()))
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}
}
