package com.synergy.backend.domain.session.entity;

import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.member.entity.Admin;
import com.synergy.backend.domain.session.dto.SessionReqDto;

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

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "conference_id")
	private Conference conference;

	@Column(nullable = false)
	private byte[] qrCode;

	@OneToMany(mappedBy = "session", fetch = LAZY, cascade = CascadeType.ALL)
	private List<AttendeeSession> attendeeSessions = new ArrayList<>();

	@ManyToMany(mappedBy = "sessions")
	private Set<Admin> admins = new HashSet<>();

	@Builder
	public Session(SessionReqDto reqDto, LocalDate progressDate, LocalDateTime startTime, LocalDateTime endTime,
				   Conference conference, Admin admin) {
		this.title = reqDto.title();
		this.speaker = reqDto.speaker();
		this.speakerPosition = reqDto.speakerPosition();
		this.progressDate = progressDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.description = reqDto.description();
		this.conference = conference;
	}


	public static Session of(SessionReqDto reqDto, LocalDate progressDate, LocalDateTime startTime, LocalDateTime endTime,
							 Conference conference) {
		return Session.builder()
				.reqDto(reqDto)
				.startTime(startTime)
				.endTime(endTime)
				.conference(conference)
				.progressDate(progressDate)
				.build();
	}

	public void updateSession(SessionReqDto reqDto, LocalDate progressDate, LocalDateTime startTime,
		LocalDateTime endTime) {
		this.title = reqDto.title();
		this.speaker = reqDto.speaker();
		this.speakerPosition = reqDto.speakerPosition();
		this.progressDate = progressDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.description = reqDto.description();
	}

	public void addQRCode(byte[] qrCode) {
		this.qrCode = qrCode;
	}

	public void addAdmin(Admin admin) {
		this.admins.add(admin);
		admin.getSessions().add(this);
	}
}
