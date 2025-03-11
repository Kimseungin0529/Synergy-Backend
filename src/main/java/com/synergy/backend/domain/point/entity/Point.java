package com.synergy.backend.domain.point.entity;

import com.synergy.backend.global.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private PointType pointType;

	private Long boothId;  // 부스 방문 시 부스 ID 저장
	private Long sessionId; // 세션 참여 시 세션 ID 저장
	private Long sessionQnAId; // 세션 Q&A 참여 시 세션 Q&A ID 저장
	private Long recruiterId; // 채용 담당자 미팅 시 담당자 ID 저장

	@Builder
	public Point(PointType pointType, Long boothId, Long sessionId, Long sessionQnAId, Long recruiterId) {
		this.pointType = pointType;
		this.boothId = boothId;
		this.sessionId = sessionId;
		this.sessionQnAId = sessionQnAId;
		this.recruiterId = recruiterId;
	}
}
