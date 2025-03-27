package com.synergy.backend.domain.meta.lookups;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "룩업 타입")
public enum LookupType {

	@Schema(description = "관심분야")
	INTERESTS("관심분야"),

	@Schema(description = "직군")
	JOB_GROUPS("직군"),

	@Schema(description = "직무")
	JOB_POSITIONS("직무");

	private final String description;

	LookupType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
