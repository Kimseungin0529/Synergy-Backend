package com.synergy.backend.domain.member.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.interest.entity.MemberInterest;
import com.synergy.backend.domain.point.entity.Point;
import com.synergy.backend.domain.session.entity.AttendeeSession;
import com.synergy.backend.domain.techstack.entity.MemberTechStack;
import com.synergy.backend.global.common.BaseEntity;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attendee extends BaseEntity implements User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "attendee_id")
	private Long id;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false, length = 25)
	private String name;

	@Column(nullable = false)
	private String phone;

	// 현재 포인트 합계
	@Column(nullable = false)
	private int totalPoints = 0;

	// 등급
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private MembershipLevelType membershipLevelType = MembershipLevelType.DEFAULT;

	@OneToMany(mappedBy = "attendee", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Point> points = new ArrayList<>();

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
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "conference_id")
	private Conference conference;

	@OneToMany(mappedBy = "attendee", cascade = CascadeType.ALL)
	private List<AttendeeSession> attendeeSessions = new ArrayList<>();

	@Builder
	private Attendee(String password, String name, String phone, String email) {
		this.password = password;
		this.name = name;
		this.phone = phone;
		this.email = email;
	}

	public static Attendee of(String email, String encodedPassword, String name, String phone) {
		return Attendee.builder()
			.email(email)
			.password(encodedPassword)
			.name(name)
			.phone(phone)
			.build();
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public RoleType getRole() {
		return RoleType.ATTENDEE;
	}

	public void addPoint(Point point) {
		points.add(point);
		point.assignAttendee(this);
	}

	public void addPoints(int point) {
		this.totalPoints += point;
		updateMembershipLevel();
	}

	private void updateMembershipLevel() {
		this.membershipLevelType = MembershipLevelType.getMembershipLevel(this.totalPoints);
	}
}
