package com.synergy.backend.domain.auth;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class LoginFailedRepository {

	public static final Integer INIT_LOGIN_TRIAL_COUNT = 0;
	public static final Integer MAX_ATTEMPT_COUNT = 5;
	private static final long LOCK_TTL_MINUTES = 30;
	private static final String PREFIX_FOR_KEY = "ULF:";

	private final RedisTemplate<String, Object> redisTemplate;

	public void setValue(String email, Integer failedCount) {
		String key = getKey(email);
		redisTemplate.opsForValue().set(key, failedCount, Duration.ofMillis(LOCK_TTL_MINUTES));
		log.info("[LoginFailedRepository]count login failed for user : {}", email);
	}

	public Integer getValues(String email) {
		Object value = redisTemplate.opsForValue().get(getKey(email));
		if (value instanceof Long) {
			return ((Long)value).intValue();
		} else if (value instanceof Integer) {
			return (Integer)value;
		} else {
			return 0;
		}
	}

	private String getKey(String email) {
		return PREFIX_FOR_KEY + email;
	}

	public Integer increment(String email) {
		Long result = redisTemplate.opsForValue().increment(getKey(email));
		return result != null ? result.intValue() : 0;
	}

	public void delete(String email) {
		redisTemplate.delete(getKey(email));
	}

	public boolean exists(String email) {
		String key = getKey(email);
		return redisTemplate.hasKey(key);
	}
}
