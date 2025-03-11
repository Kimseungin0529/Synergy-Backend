package com.synergy.backend.domain.booth.model;

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
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
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

	public void addAdmin(Admin admin) {
		this.admins.add(admin);
		admin.getBooths().add(this);
	}

}
