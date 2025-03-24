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

	@Column(nullable = false, length = 10)
	private String companyName;

	@Column(nullable = false, length = 20)
	private String companyType;

	@Column(nullable = false)
	private String boothLocation;

	@Column(nullable = false)
	private Integer boothNumber;

	@Column(nullable = false, length = 150)
	private String boothDescription;

	@Lob
	private byte[] image;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "conference_id")
	private Conference conference;

	@ManyToMany(mappedBy = "booths")
	private Set<Admin> admins = new HashSet<>();

	@Lob
	private byte[] qrCode;

	public Booth(String companyName, String companyType, String boothLocation, Integer boothNumber,
				 String boothDescription, Conference conference, byte[] image) {
		this.companyName = companyName;
		this.companyType = companyType;
		this.boothLocation = boothLocation;
		this.boothNumber = boothNumber;
		this.boothDescription = boothDescription;
		this.conference = conference;
		this.image = image;
	}

	public void addAdmin(Admin admin) {
		this.admins.add(admin);
		admin.getBooths().add(this);
	}

	public void updateInfo(String companyName, String companyType, String boothLocation, Integer boothNumber,
						   String boothDescription, byte[] image) {
		this.companyName = companyName;
		this.companyType = companyType;
		this.boothLocation = boothLocation;
		this.boothNumber = boothNumber;
		this.boothDescription = boothDescription;
		this.image = image;
	}
}
