package com.synergy.backend.global.token;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.synergy.backend.global.jwt.JwtProperties;

@ExtendWith(MockitoExtension.class)
class TokenServiceImplTest {

	@InjectMocks
	TokenServiceImpl tokenService;

	@Mock
	private RedisTemplate<String, String> redisTemplate;

	@Mock
	private ValueOperations<String, String> valueOperations;

	@Mock
	private JwtProperties jwtProperties;

	@BeforeEach
	void setUp() {
		tokenService = new TokenServiceImpl(redisTemplate, jwtProperties);
	}

	@DisplayName("리프레시 토큰을 Redis에 저장한다.")
	@Test
	void storeRefreshToken() {
		// given
		String identifier = "user@example.com";
		String token = "refreshToken";

		when(jwtProperties.refreshTokenExpiration()).thenReturn(Duration.ofDays(30));
		when(redisTemplate.opsForValue()).thenReturn(valueOperations);

		// when
		tokenService.storeRefreshToken(identifier, token);

		// then
		verify(valueOperations).set("refresh:" + identifier, token, Duration.ofDays(30));
	}

	@DisplayName("Redis에서 리프레시 토큰을 조회한다.")
	@Test
	void getStoredRefreshToken() {
		// given
		String identifier = "user@example.com";
		String token = "refreshToken";
		when(valueOperations.get("refresh:" + identifier)).thenReturn(token);
		when(redisTemplate.opsForValue()).thenReturn(valueOperations);

		// when
		String result = tokenService.getStoredRefreshToken(identifier);

		// then
		assertThat(result).isEqualTo(token);
	}

	@DisplayName("Redis에서 리프레시 토큰을 삭제한다.")
	@Test
	void deleteRefreshToken() {
		// given
		String identifier = "user@example.com";

		// when
		tokenService.deleteRefreshToken(identifier);

		// then
		verify(redisTemplate).delete("refresh:" + identifier);
	}
}
