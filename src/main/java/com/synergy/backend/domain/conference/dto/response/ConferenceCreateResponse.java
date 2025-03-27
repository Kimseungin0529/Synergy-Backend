package com.synergy.backend.domain.conference.dto.response;

import com.synergy.backend.domain.conference.entity.Conference;

public record ConferenceCreateResponse(
	Long id,
	String ticketCode
) {
	public static ConferenceCreateResponse from(Conference conference) {
		return new ConferenceCreateResponse(conference.getId(), conference.getTicketCode());
	}
}
