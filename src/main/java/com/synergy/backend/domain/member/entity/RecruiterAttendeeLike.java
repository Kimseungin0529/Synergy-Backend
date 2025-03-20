package com.synergy.backend.domain.member.entity;

import com.synergy.backend.global.common.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "recruiter_attendee_like",
	uniqueConstraints = @UniqueConstraint(
		name = "unique_recruiter_attendee",
		columnNames = {"recruiter_id", "attendee_id"}
	))
public class RecruiterAttendeeLike extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "recruiter_id", nullable = false)
	private Recruiter recruiter;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attendee_id", nullable = false)
	private Attendee attendee;

	@Builder
	public RecruiterAttendeeLike(Recruiter recruiter, Attendee attendee) {
		this.recruiter = recruiter;
		this.attendee = attendee;
	}

	public static RecruiterAttendeeLike of(Recruiter recruiter, Attendee attendee) {
		return RecruiterAttendeeLike
			.builder()
			.attendee(attendee)
			.recruiter(recruiter)
			.build();
	}
}
