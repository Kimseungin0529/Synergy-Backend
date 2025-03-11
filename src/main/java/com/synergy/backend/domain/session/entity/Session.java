package com.synergy.backend.domain.session.entity;

import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.member.entity.Admin;
import com.synergy.backend.domain.session.dto.SessionReqDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

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

    @NotNull
    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false, length = 3000)
    private String description;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "conference_id")
    private Conference conference;

    @Builder
    public Session(SessionReqDto reqDto, LocalDate progressDate, LocalDateTime startTime, LocalDateTime endTime, Conference conference) {
        this.title = reqDto.title();
        this.speaker = reqDto.speaker();
        this.speakerPosition = reqDto.speakerPosition();
        this.progressDate = progressDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = reqDto.description();
        this.conference = conference;
    }
	@ManyToMany(mappedBy = "sessions")
	private Set<Admin> admins = new HashSet<>();

	@Builder
	public Session(SessionReqDto reqDto, LocalDateTime startTime, LocalDateTime endTime, Conference conference) {
		this.title = reqDto.title();
		this.speaker = reqDto.speaker();
		this.startTime = startTime;
		this.endTime = endTime;
		this.description = reqDto.description();
		this.conference = conference;
	}

    public void updateSession(SessionReqDto reqDto, LocalDate progressDate, LocalDateTime startTime, LocalDateTime endTime) {
        this.title = reqDto.title();
        this.speaker = reqDto.speaker();
        this.speakerPosition = reqDto.speakerPosition();
        this.progressDate = progressDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = reqDto.description();
    }
	public void addAdmin(Admin admin) {
		this.admins.add(admin);
		admin.getSessions().add(this);
	}

	public void updateSession(SessionReqDto reqDto, LocalDateTime startTime, LocalDateTime endTime) {
		this.title = reqDto.title();
		this.speaker = reqDto.speaker();
		this.startTime = startTime;
		this.endTime = endTime;
		this.description = reqDto.description();
	}
}
