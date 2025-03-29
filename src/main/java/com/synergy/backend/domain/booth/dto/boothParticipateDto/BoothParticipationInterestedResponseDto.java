package com.synergy.backend.domain.booth.dto.boothParticipateDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class BoothParticipationInterestedResponseDto {
	private Long boothId;
	private String companyName;
	private String companyType;
	private String boothLocation;
	private String boothNumber;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd")
	private LocalDate progressDate;
	private List<BoothParticipateInterestedTechDto> techs;

	@QueryProjection
	public BoothParticipationInterestedResponseDto(Long boothId, String companyName, String companyType, String boothLocation,
												   String boothNumber, LocalDate progressDate) {
		this.boothId = boothId;
		this.companyName = companyName;
		this.companyType = companyType;
		this.boothLocation = boothLocation;
		this.boothNumber = boothNumber;
		this.progressDate = progressDate;
		this.techs = new ArrayList<>();
	}

	public void addTechs(List<BoothParticipateInterestedTechDto> techs) {
		this.techs = techs;
	}
}
