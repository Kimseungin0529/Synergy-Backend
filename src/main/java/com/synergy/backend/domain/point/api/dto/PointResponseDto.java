package com.synergy.backend.domain.point.api.dto;

import java.time.LocalDateTime;

import com.synergy.backend.domain.point.entity.Point;

public record PointResponseDto(
	String title,
	Integer point,
	String details,
	LocalDateTime createdTime
) {
	public static PointResponseDto from(Point point) {
		return new PointResponseDto(
			point.getPointType().getMessage(),
			point.getPointType().getPointValue(),
			point.getDetails(),
			point.getCreatedTime()
		);
	}
}
