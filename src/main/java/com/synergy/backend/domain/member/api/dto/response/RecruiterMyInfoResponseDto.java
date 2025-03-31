package com.synergy.backend.domain.member.api.dto.response;

import com.synergy.backend.domain.member.entity.Recruiter;

public record RecruiterMyInfoResponseDto(
	String companyPhotoUrl,
	String recruiterName,
	String company,
	String responsibility
) {
	public static RecruiterMyInfoResponseDto from(Recruiter recruiter) {
		return new RecruiterMyInfoResponseDto(
			recruiter.getCompanyPhotoUrl(),
			recruiter.getName(),
			recruiter.getCompany(),
			recruiter.getResponsibility()
		);
	}

	public static RecruiterMyInfoResponseDto from(String companyPhotoUrl, String recruiterName, String company,
		String responsibility) {
		return new RecruiterMyInfoResponseDto(
			companyPhotoUrl, recruiterName, company, responsibility);
	}
}
