package com.synergy.backend.domain.member.service;

public interface RecruiterLikeService {
	void likeAttendee(Long id, Long attendeeId);

	void unlikeAttendee(Long id, Long attendeeId);
}
