package com.synergy.backend.domain.booth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

public class BoothParticipationResponseDto {
	private Long boothId;
	private String companyName;
	private String companyType;
	private String boothLocation;
	private String boothNumber;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd")
	private LocalDate progressDate;
	private List<BoothParticipateInterestedTechDto> techs;
}
