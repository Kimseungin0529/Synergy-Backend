package com.synergy.backend.global.token;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.synergy.backend.global.jwt.JwtProperties;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

	private static final String REFRESH_PREFIX = "refresh:";

	private final RedisTemplate<String, String> redisTemplate;
	private final JwtProperties jwtProperties;

	@Override
	public void storeRefreshToken(String identifier, String refreshToken) {
		redisTemplate.opsForValue().set(REFRESH_PREFIX + identifier, refreshToken, jwtProperties.refreshTokenExpiration());
	}

	@Override
	public String getStoredRefreshToken(String identifier) {
		return redisTemplate.opsForValue().get(REFRESH_PREFIX + identifier);
	}

	@Override
	public void deleteRefreshToken(String identifier) {
		redisTemplate.delete(REFRESH_PREFIX + identifier);
	}

}
