package com.synergy.backend.domain.member.api.dto.response;

import com.synergy.backend.domain.member.entity.Recruiter;

public record LikedRecruiterResponseDto(
	String company,
	String responsibility,
	String name
) {
	public static LikedRecruiterResponseDto from(Recruiter recruiter) {
		return new LikedRecruiterResponseDto(
			recruiter.getCompany(),
			recruiter.getResponsibility(),
			recruiter.getName()
		);
	}
}
