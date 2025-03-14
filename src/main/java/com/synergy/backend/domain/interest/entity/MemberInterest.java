package com.synergy.backend.domain.interest.entity;

import com.synergy.backend.domain.member.entity.Attendee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class MemberInterest {

	@Id
	@Column(name = "member_interest_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Attendee attendee;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "interest_id", nullable = false)
	private Interest interest;

	@Builder
	public MemberInterest(Attendee attendee, Interest interest) {
		this.attendee = attendee;
		this.interest = interest;
	}
}
