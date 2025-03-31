package com.synergy.backend.domain.member.service;

import java.util.List;

import com.synergy.backend.domain.member.api.dto.response.AttendeeSimpleResponseDto;
import com.synergy.backend.domain.member.api.dto.response.LikedRecruiterResponseDto;

public interface RecruiterAttendeeLikeService {
	void likeAttendee(Long id, Long attendeeId);

	void unlikeAttendee(Long id, Long attendeeId);

	List<AttendeeSimpleResponseDto> getLikedAttendees(Long recruiterId);

	List<LikedRecruiterResponseDto> getLikedRecruiters(Long attendeeId);
}
