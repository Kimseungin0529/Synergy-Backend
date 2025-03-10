package com.synergy.backend.domain.member.entity;

import com.synergy.backend.global.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recruiter extends BaseEntity implements User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "recruiter_id")
	private Long id;

	@Column(nullable = false)
	private String assignedAdminId;

	// 회사
	@Column
	private String company;

	// 담당 업무
	@Column
	private String Responsibility;

	@Override
	public RoleType getRole() {
		return RoleType.RECRUITER;
	}
}
