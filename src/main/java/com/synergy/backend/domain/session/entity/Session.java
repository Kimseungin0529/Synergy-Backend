package com.synergy.backend.domain.session.entity;

import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.member.entity.Admin;
import com.synergy.backend.domain.session.dto.sessionDto.SessionReqDto;

import com.synergy.backend.domain.session.exception.NotValidSessionTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Session {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "session_id")
	private Long id;

	@NotNull
	@Column(nullable = false, length = 100)
	private String title;

	@Column(nullable = false, length = 100)
	private String speaker;

	@Column(nullable = false, length = 20)
	private String speakerPosition;

	@Column(nullable = false)
	private LocalDate progressDate;

	@Column(nullable = false)
	private LocalDateTime startTime;

	@Column(nullable = false)
	private LocalDateTime endTime;

	@Column(nullable = false, length = 3000)
	private String description;

	@Column(nullable = false)
	private Integer maximum;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "conference_id")
	private Conference conference;

	@Column(nullable = false)
	private String secretCode;

	@Column(nullable = false)
	private String fileUrl;

	@OneToMany(mappedBy = "session", fetch = LAZY, cascade = CascadeType.ALL)
	private List<AttendeeSession> attendeeSessions = new ArrayList<>();

	@ManyToMany(mappedBy = "sessions")
	private Set<Admin> admins = new HashSet<>();

	@Builder
	public Session(SessionReqDto reqDto, String secretCode, Conference conference) {
		this.title = reqDto.title();
		this.speaker = reqDto.speaker();
		this.speakerPosition = reqDto.speakerPosition();
		this.progressDate = reqDto.progressDate();
		this.startTime = reqDto.startTime();
		this.endTime = reqDto.endTime();
		this.description = reqDto.description();
		this.maximum = reqDto.maximum();
		this.secretCode = secretCode;
		this.conference = conference;
	}


	public static Session of(SessionReqDto reqDto, String secretCode, Conference conference) {
		return Session.builder()
				.reqDto(reqDto)
				.conference(conference)
				.secretCode(secretCode)
				.build();
	}

	public void updateSession(SessionReqDto reqDto) {
		this.title = reqDto.title();
		this.speaker = reqDto.speaker();
		this.speakerPosition = reqDto.speakerPosition();
		this.progressDate = reqDto.progressDate();
		this.startTime = reqDto.startTime();
		this.endTime = reqDto.endTime();
		this.description = reqDto.description();
	}

	public void addQRCode(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public void addAdmin(Admin admin) {
		this.admins.add(admin);
		admin.getSessions().add(this);
	}
}
