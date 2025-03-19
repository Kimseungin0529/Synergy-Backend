package com.synergy.backend.domain.member.entity.details;

import java.util.function.Supplier;

import com.synergy.backend.domain.member.exception.InvalidConferenceParticipationPurposeCodeException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConferenceParticipationPurpose implements BaseAttendeeDetailEnum {
	EMPLOYMENT_AND_NETWORK_EXPANSION(1, "취업 및 인맥 확장"),
	LEARNING_LATEST_TECH_AND_TRENDS(2, "최신 기술 및 트렌드 학습"),
	PERSONAL_INTERESTS_AND_CURIOSITY(3, "개인적인 관심사 및 호기심"),
	EDUCATIONAL_AND_RESEARCH(4, "교육 및 연구 목적");

	private final Integer code;
	private final String description;
	private final Supplier<? extends RuntimeException> exceptionSupplier = InvalidConferenceParticipationPurposeCodeException::new;

	public static ConferenceParticipationPurpose fromCode(int code) {
		return BaseAttendeeDetailEnum.fromCode(ConferenceParticipationPurpose.class, code);
	}
}
