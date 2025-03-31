package com.synergy.backend.domain.point.entity;

import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.global.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

	private String details;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attendee_id")
	private Attendee attendee;

	@Builder
	private Point(PointType pointType, String details) {
		this.pointType = pointType;
		this.details = details;
	}

	public static Point of(PointType pointType, String details) {
		return Point.builder()
			.pointType(pointType)
			.details(details)
			.build();
	}

	public void assignAttendee(Attendee attendee) {
		this.attendee = attendee;
	}

}
