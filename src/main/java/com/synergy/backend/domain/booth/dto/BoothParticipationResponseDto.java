package com.synergy.backend.domain.booth.dto;

import java.util.List;

public class BoothParticipationResponseDto {
	private Long boothId;
	private String companyName;
	private String companyType;
	private String boothLocation;
	private String boothNumber;
	private List<BoothParticipateInterestedTechDto> techs;
}
