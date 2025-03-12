package com.synergy.backend.domain.booth.entity;

import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.*;

import java.util.HashSet;
import java.util.Set;

import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.member.entity.Admin;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
public class Booth {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "booth_id")
	private Long id;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(nullable = false, length = 100)
	private String company;

	@Column(nullable = false)
	private String location;

	@Column(nullable = false, length = 1000)
	private String description;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "conference_id")
	private Conference conference;

	@ManyToMany(mappedBy = "booths")
	private Set<Admin> admins = new HashSet<>();

    @Lob
	private byte[] qrCode;

	public Booth(String name, String company, String location, String description, Conference conference) {
		this.name = name;
		this.company = company;
		this.location = location;
		this.description = description;
		this.conference = conference;
	}

	public void addAdmin(Admin admin) {
		this.admins.add(admin);
		admin.getBooths().add(this);
	}

	public void updateInfo(String name, String company, String location, String description) {
		this.name = name;
		this.company = company;
		this.location = location;
		this.description = description;
	}
}
