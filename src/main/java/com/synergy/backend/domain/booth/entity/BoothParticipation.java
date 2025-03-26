package com.synergy.backend.domain.booth.entity;

import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.*;

import com.synergy.backend.domain.member.entity.Attendee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class BoothParticipation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "attendee_booth_id")
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "booth_id")
	private Booth booth;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "attendee_id", nullable = false)
	private Attendee attendee;

	@Builder
	private BoothParticipation(Booth booth, Attendee attendee) {
		this.booth = booth;
		this.attendee = attendee;
	}

	public static BoothParticipation of(Booth booth, Attendee attendee) {
		return BoothParticipation.builder()
			.booth(booth)
			.attendee(attendee)
			.build();
	}
}
