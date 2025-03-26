package com.synergy.backend.domain.booth.entity;

import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.*;

import java.util.HashSet;
import java.util.Set;

import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.member.entity.Admin;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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

	@Column(nullable = false)
	private String companyName;

	@Column(nullable = false)
	private String companyType;

	@Column(nullable = false)
	private String boothLocation;

	@Column(nullable = false)
	private String boothNumber;

	@Column(nullable = false)
	private String boothDescription;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "conference_id")
	private Conference conference;

	@ManyToMany(mappedBy = "booths")
	private Set<Admin> admins = new HashSet<>();

	@Column(nullable = false, unique = true)
	private String secretCode;

	@Column(nullable = false)
	private String qrKey;

	@Column(nullable = false)
	private String qrUrl;

	@Column(nullable = false)
	private String imageKey;

	@Column(nullable = false)
	private String imageUrl;

	public void addAdmin(Admin admin) {
		this.admins.add(admin);
		admin.getBooths().add(this);
	}

	public Booth(String companyName, String companyType, String boothLocation, String boothNumber,
				 String boothDescription, Conference conference) {
		this.companyName = companyName;
		this.companyType = companyType;
		this.boothLocation = boothLocation;
		this.boothNumber = boothNumber;
		this.boothDescription = boothDescription;
		this.conference = conference;
	}

	public void updateInfo(String companyName, String companyType, String boothLocation, String boothNumber,
						   String boothDescription, String imageKey, String imageUrl) {
		this.companyName = companyName;
		this.companyType = companyType;
		this.boothLocation = boothLocation;
		this.boothNumber = boothNumber;
		this.boothDescription = boothDescription;
		this.imageKey = imageKey;
		this.imageUrl = imageUrl;
	}
}

