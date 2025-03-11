package com.synergy.backend.domain.member.entity;

import static jakarta.persistence.FetchType.*;

import java.util.List;
import java.util.Set;

import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.interest.entity.MemberInterest;
import com.synergy.backend.domain.point.entity.Point;
import com.synergy.backend.domain.techstack.entity.MemberTechStack;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
public class Attendee extends Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	@Column(nullable = false)
	private String phone;

	// 현재 포인트 합계
	@Column(nullable = false)
	@Builder.Default
	private int totalPoints = 0;

	// 등급
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private MembershipLevelType membershipLevelType = MembershipLevelType.BRONZE;

	@OneToMany(mappedBy = "attendee", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Point> points;

	// 현재 직업
	@Enumerated(EnumType.STRING)
	private OccupationType occupationType;

	// 현재 직무
	@Enumerated(EnumType.STRING)
	private PositionType position;

	// 희망 직무
	@Enumerated(EnumType.STRING)
	private PositionType desiredPosition;

	// 경력
	private String yearsOfExperience;

	// 참가자-보유기술
	@OneToMany(mappedBy = "attendee", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<MemberTechStack> memberTechStacks;

	// 자기소개서
	private String selfIntroduction;

	// 채용 희망여부
	private boolean isHiringInterested;

	// 경력 정보
	private String personalHistory;

	// 경험 및 기타 정보
	private String information;

	// 참가자-관심분야
	@OneToMany(mappedBy = "attendee", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<MemberInterest> memberInterests;

	// 컨퍼런스
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "conference_id")
	private Conference conference;

}
