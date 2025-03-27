package com.synergy.backend.domain.booth.entity;

import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.member.entity.Admin;

import com.synergy.backend.global.util.file.dto.FileInformationDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
//@Setter
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
	private LocalDate progressDate;

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
				 LocalDate progressDate, String secretCode, String boothDescription, Conference conference) {
		this.companyName = companyName;
		this.companyType = companyType;
		this.boothLocation = boothLocation;
		this.boothNumber = boothNumber;
		this.progressDate = progressDate;
		this.boothDescription = boothDescription;
		this.secretCode = secretCode;
		this.conference = conference;
	}

	public void updateInfo(String companyName, String companyType, String boothLocation, String boothNumber,
			   	LocalDate progressDate, String boothDescription, String imageKey, String imageUrl) {
		this.companyName = companyName;
		this.companyType = companyType;
		this.boothLocation = boothLocation;
		this.boothNumber = boothNumber;
		this.progressDate = progressDate;
		this.boothDescription = boothDescription;
		this.imageKey = imageKey;
		this.imageUrl = imageUrl;
	}

	public void updateQr(FileInformationDto fileInformation) {
		this.qrKey = fileInformation.fileKey();
		this.qrUrl = fileInformation.accessUrl();
	}

	public void updateImage(FileInformationDto fileInformation){
		this.imageKey = fileInformation.fileKey();
		this.imageUrl = fileInformation.accessUrl();
	}
}

