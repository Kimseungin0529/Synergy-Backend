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
import com.synergy.backend.global.util.file.dto.FileInformationDto;
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
	private String qrKey;

	@Column(nullable = false)
	private String qrUrl;

	@Column(nullable = false)
	private String imageKey;

	@Column(nullable = false)
	private String imageUrl;

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

	public void updateSession(SessionReqDto reqDto, FileInformationDto fileInfo) {
		this.title = reqDto.title();
		this.speaker = reqDto.speaker();
		this.speakerPosition = reqDto.speakerPosition();
		this.progressDate = reqDto.progressDate();
		this.startTime = reqDto.startTime();
		this.endTime = reqDto.endTime();
		this.description = reqDto.description();
		this.maximum = reqDto.maximum();
		this.imageKey = fileInfo.fileKey();
		this.imageUrl = fileInfo.accessUrl();
	}

	public void addQRCode(FileInformationDto fileInformationDto) {
		this.qrKey = fileInformationDto.fileKey();
		this.qrUrl = fileInformationDto.accessUrl();
	}

	public void addImage(FileInformationDto fileInformationDto) {
		this.imageKey = fileInformationDto.fileKey();
		this.imageUrl = fileInformationDto.accessUrl();
	}

	public void addAdmin(Admin admin) {
		this.admins.add(admin);
		admin.getSessions().add(this);
	}

	public void removeAllAdmins() {
		for (Admin admin : this.admins) {
			admin.getSessions().remove(this); // 양방향 관계 제거
		}
		this.admins.clear();
	}
}
