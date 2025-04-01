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
	private String boothDescription;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate progressDate;
	private String qrCode;
	private List<BoothParticipateInterestedTechDto> dataset;

	@QueryProjection
	public BoothParticipationInterestedResponseDto(Long boothId, String companyName, String companyType, String boothLocation,
												   String boothNumber, String boothDescription, LocalDate progressDate, String qrCode) {
		this.boothId = boothId;
		this.companyName = companyName;
		this.companyType = companyType;
		this.boothLocation = boothLocation;
		this.boothNumber = boothNumber;
		this.boothDescription = boothDescription;
		this.progressDate = progressDate;
		this.qrCode = qrCode;
		this.dataset = new ArrayList<>();
	}

	public BoothParticipationInterestedResponseDto(Long boothId, String companyName, String companyType, String boothLocation,
												   String boothNumber, String boothDescription, LocalDate progressDate, String qrCode, List<BoothParticipateInterestedTechDto> dataset) {
		this.boothId = boothId;
		this.companyName = companyName;
		this.companyType = companyType;
		this.boothLocation = boothLocation;
		this.boothNumber = boothNumber;
		this.boothDescription = boothDescription;
		this.progressDate = progressDate;
		this.qrCode = qrCode;
		this.dataset = dataset;
	}

	public void addTechs(List<BoothParticipateInterestedTechDto> techs) {
		this.dataset = techs;
	}
}
