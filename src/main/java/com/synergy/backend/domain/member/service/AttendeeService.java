package com.synergy.backend.domain.member.service;

import java.util.Set;

import com.synergy.backend.domain.interest.entity.Interest;
import com.synergy.backend.domain.member.api.dto.request.JobInfoDetailsRequestDto;
import com.synergy.backend.domain.member.api.dto.request.JobInfoRequestDto;
import com.synergy.backend.domain.member.api.dto.resposne.AttendeeFullInfoResponseDto;
import com.synergy.backend.domain.member.api.dto.resposne.MyInfoResponseDto;
import com.synergy.backend.domain.member.entity.RoleType;

public interface AttendeeService {

	Set<Interest> addInterests(String email, Set<Integer> interestCodes);

	void addJobInfo(String email, JobInfoRequestDto request);

	void addJobInfoDetails(String email, JobInfoDetailsRequestDto request);

	MyInfoResponseDto getMyInformation(String identifier);

	AttendeeFullInfoResponseDto getAttendeeInfoDetail(Long attendeeId, String identifier, RoleType role);
}
