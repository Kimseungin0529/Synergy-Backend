package com.synergy.backend.domain.member.repository;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.synergy.backend.domain.member.api.dto.response.AttendeePointRankingResponseDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AttendeeRedisRankingRepositoryImpl implements AttendeeRedisRankingRepository {

	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final String RANKING_KEY_PREFIX = "conference:ranking:";

	private String getKey(Long conferenceId) {
		return RANKING_KEY_PREFIX + conferenceId;
	}

	@Override
	public void saveRanking(Long conferenceId, AttendeePointRankingResponseDto dto) {
		String key = getKey(conferenceId);
		try {
			String json = objectMapper.writeValueAsString(dto);
			redisTemplate.opsForZSet().add(key, json, dto.totalPoints());

			Long ttl = redisTemplate.getExpire(key);
			if (ttl == null || ttl == -1 || ttl == -2) {
				redisTemplate.expire(key, Duration.ofHours(1));
			}

		} catch (JsonProcessingException e) {
			throw new RuntimeException("Failed to serialize AttendeeRankingDto", e);
		}
	}

	@Override
	public List<AttendeePointRankingResponseDto> getRanking(Long conferenceId, long start, long end) {
		String key = getKey(conferenceId);
		Set<Object> result = redisTemplate.opsForZSet().reverseRange(key, start, end);
		if (result == null)
			return List.of();

		return result.stream().map(obj -> {
			try {
				return objectMapper.readValue(obj.toString(), AttendeePointRankingResponseDto.class);
			} catch (JsonProcessingException e) {
				throw new RuntimeException("Failed to deserialize AttendeePointRankingResponseDto", e);
			}
		}).toList();
	}

	@Override
	public Page<AttendeePointRankingResponseDto> getRankingPage(Long conferenceId, Pageable pageable) {
		String key = getKey(conferenceId);
		long start = pageable.getOffset();
		long end = start + pageable.getPageSize() - 1;

		Set<Object> result = redisTemplate.opsForZSet().reverseRange(key, start, end);
		Long total = redisTemplate.opsForZSet().zCard(key);

		List<AttendeePointRankingResponseDto> dtos = result == null
			? List.of()
			: result.stream().map(o -> {
			try {
				return objectMapper.readValue(o.toString(), AttendeePointRankingResponseDto.class);
			} catch (JsonProcessingException e) {
				throw new RuntimeException("Failed to deserialize", e);
			}
		}).toList();

		return new PageImpl<>(dtos, pageable, total != null ? total : 0);
	}

	@Override
	public void deleteRanking(Long conferenceId) {
		redisTemplate.delete(getKey(conferenceId));
	}

	@Override
	public Long getTotalCount(Long conferenceId) {
		return redisTemplate.opsForZSet().zCard(getKey(conferenceId));
	}
}
