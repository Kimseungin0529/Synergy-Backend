package com.synergy.backend.domain.member.entity;

import java.util.HashSet;
import java.util.Set;

import com.synergy.backend.domain.booth.entity.Booth;
import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.session.entity.Session;
import com.synergy.backend.global.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin extends BaseEntity implements User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "admin_id")
	private Long id;

	@Column(nullable = false)
	private String adminAuthCode;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "ADMIN_CONFERENCE",
		joinColumns = @JoinColumn(name = "admin_id"),
		inverseJoinColumns = @JoinColumn(name = "conference_id")
	)
	private Set<Conference> conferences = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "ADMIN_BOOTH",
		joinColumns = @JoinColumn(name = "admin_id"),
		inverseJoinColumns = @JoinColumn(name = "booth_id")
	)
	private Set<Booth> booths = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "ADMIN_SESSION",
		joinColumns = @JoinColumn(name = "admin_id"),
		inverseJoinColumns = @JoinColumn(name = "session_id")
	)
	private Set<Session> sessions = new HashSet<>();

	@Builder
	private Admin(String adminAuthCode) {
		this.adminAuthCode = adminAuthCode;
	}

	public static Admin of(String adminAuthCode) {
		return Admin.builder()
			.adminAuthCode(adminAuthCode)
			.build();
	}

	@Override
	public RoleType getRole() {
		return RoleType.ADMIN;
	}

	@Override
	public String getIdentifier() {
		return this.adminAuthCode;
	}

	public void addSession(Session session) {
		this.sessions.add(session);
		session.getAdmins().add(this);
	}

	public void addBooth(Booth booth) {
		this.booths.add(booth);
		booth.getAdmins().add(this);
	}

	public void addConference(Conference conference) {
		this.conferences.add(conference);
		conference.getAdmins().add(this);
	}
}
