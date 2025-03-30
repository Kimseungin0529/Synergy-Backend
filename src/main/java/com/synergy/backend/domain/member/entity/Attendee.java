package com.synergy.backend.domain.member.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.interest.entity.AttendeeInterest;
import com.synergy.backend.domain.job.JobGroup;
import com.synergy.backend.domain.job.JobPosition;
import com.synergy.backend.domain.member.entity.details.AgeGroup;
import com.synergy.backend.domain.member.entity.details.ConferenceParticipationPurpose;
import com.synergy.backend.domain.member.entity.details.EducationLevelType;
import com.synergy.backend.domain.member.entity.details.ExperienceLevelType;
import com.synergy.backend.domain.member.entity.details.MembershipLevelType;
import com.synergy.backend.domain.member.entity.details.PreferredCorporateCulture;
import com.synergy.backend.domain.member.entity.details.RegionType;
import com.synergy.backend.domain.member.entity.details.WorkplaceSelectionFactor;
import com.synergy.backend.domain.point.entity.Point;
import com.synergy.backend.domain.session.entity.AttendeeSession;
import com.synergy.backend.global.common.BaseEntity;
import com.synergy.backend.global.util.file.dto.FileInformationDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
	indexes = {
		@Index(name = "idx_total_points", columnList = "total_points DESC")
	})
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

	// 계정 잠금 상태
	@Column(nullable = false)
	private boolean isLocked = false;

	// 현재 포인트 합계
	@Column(nullable = false)
	private int totalPoints = 0;

	// 등급
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private MembershipLevelType membershipLevelType = MembershipLevelType.DEFAULT;

	@OneToMany(mappedBy = "attendee", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Point> points = new ArrayList<>();

	/*------Job Info------*/
	// 현재 직군
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "current_job_group_id")
	private JobGroup currentJobGroup;

	// 현재 직무
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "current_job_position_id")
	private JobPosition currentJobPosition;

	// 채용 희망여부
	private Boolean isHiringInterested = false;

	/*------Job Info Details------*/
	// 희망 직무
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "desired_job_group_id")
	private JobGroup desiredJobGroup;

	// 희망 직무
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "desired_job_position_id")
	private JobPosition desiredJobPosition;

	// 학력
	@Enumerated(EnumType.STRING)
	private EducationLevelType educationLevel;

	// 연령대
	@Enumerated(EnumType.STRING)
	private AgeGroup ageGroup;

	// 보유기술
	private String techStacks;

	// 경력
	@Enumerated(EnumType.STRING)
	private ExperienceLevelType experienceLevel;

	// 희망 근무 지역
	@ElementCollection
	@CollectionTable(name = "attendee_desired_work_region",
		joinColumns = @JoinColumn(name = "attendee_id"))
	@Enumerated(EnumType.ORDINAL)
	private Set<RegionType> desiredWorkRegion = new HashSet<>();

	// 자기소개서
	@Column(length = 1000)
	private String selfIntroduction;

	// 프로필 사진
	private String profileImageKey;

	@Column(length = 1024)
	private String profileImageUrl;

	// 경험 및 기타 정보
	private String information;

	// 직장 선택 요소
	@ElementCollection
	@CollectionTable(name = "attendee_workplace_selection_factors",
		joinColumns = @JoinColumn(name = "attendee_id"))
	@Enumerated(EnumType.ORDINAL)
	private Set<WorkplaceSelectionFactor> workplaceSelectionFactors = new HashSet<>();

	// 선호하는 기업 문화
	@ElementCollection
	@CollectionTable(name = "attendee_preferred_corporate_cultures",
		joinColumns = @JoinColumn(name = "attendee_id"))
	@Enumerated(EnumType.ORDINAL)
	private Set<PreferredCorporateCulture> preferredCorporateCultures = new HashSet<>();

	// 컨퍼런스 참여 목적
	@ElementCollection
	@CollectionTable(name = "attendee_conference_participation_purposes",
		joinColumns = @JoinColumn(name = "attendee_id"))
	@Enumerated(EnumType.ORDINAL)
	private Set<ConferenceParticipationPurpose> conferenceParticipationPurposes = new HashSet<>();

	// 참가자-관심분야
	@OneToMany(mappedBy = "attendee", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<AttendeeInterest> attendeeInterests = new HashSet<>();

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

	@Override
	public String getIdentifier() {
		return this.email;
	}

	public void updateJobInfo(JobPosition jobPosition, JobGroup jobGroup,
		Boolean isHiringInterested) {
		this.currentJobPosition = jobPosition;
		this.currentJobGroup = jobGroup;
		this.isHiringInterested = isHiringInterested;
	}

	public void updateJobInfoDetails(
		JobGroup desiredJobGroup,
		JobPosition desiredJobPosition,
		EducationLevelType educationLevel,
		AgeGroup ageGroup,
		String techStacks,
		ExperienceLevelType experienceLevel,
		Set<RegionType> desiredWorkRegion,
		String selfIntroduction,
		String information,
		Set<WorkplaceSelectionFactor> workplaceSelectionFactors,
		Set<PreferredCorporateCulture> preferredCorporateCultures,
		Set<ConferenceParticipationPurpose> conferenceParticipationPurposes
	) {
		this.desiredJobGroup = desiredJobGroup;
		this.desiredJobPosition = desiredJobPosition;
		this.educationLevel = educationLevel;
		this.ageGroup = ageGroup;
		this.techStacks = techStacks;
		this.experienceLevel = experienceLevel;
		this.desiredWorkRegion = desiredWorkRegion != null ? desiredWorkRegion : new HashSet<>();
		this.selfIntroduction = selfIntroduction;
		this.information = information;
		this.attendeeInterests =
			attendeeInterests != null ? attendeeInterests : new HashSet<>();
		this.workplaceSelectionFactors =
			workplaceSelectionFactors != null ? workplaceSelectionFactors : new HashSet<>();
		this.preferredCorporateCultures =
			preferredCorporateCultures != null ? preferredCorporateCultures : new HashSet<>();
		this.conferenceParticipationPurposes =
			conferenceParticipationPurposes != null ? conferenceParticipationPurposes : new HashSet<>();
	}

	public void addPoint(Point point) {
		points.add(point);
		point.assignAttendee(this);
	}

	public void addTotalPoints(int point) {
		this.totalPoints += point;
		updateMembershipLevel();
	}

	private void updateMembershipLevel() {
		this.membershipLevelType = MembershipLevelType.getMembershipLevel(this.totalPoints);
	}

	public void updatePassword(String newEncodePassword) {
		this.password = newEncodePassword;
	}

	public void addImage(FileInformationDto fileInformationDto) {
		this.profileImageKey = fileInformationDto.fileKey();
		this.profileImageUrl = fileInformationDto.accessUrl();
	}

	public void assignConference(Conference conference) {
		this.conference = conference;
	}

	public void lockAccount() {
		this.isLocked = true;
	}

	public void unlockAccount() {
		this.isLocked = false;
	}
}
