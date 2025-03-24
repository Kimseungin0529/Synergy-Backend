package com.synergy.backend.domain.member.service;

import com.synergy.backend.domain.member.api.dto.request.JobInfoDetailsRequestDto;
import com.synergy.backend.domain.member.api.dto.request.JobInfoRequestDto;
import com.synergy.backend.domain.member.api.dto.resposne.AttendeeFullInfoResponseDto;
import com.synergy.backend.domain.member.api.dto.resposne.MyInfoResponseDto;
import com.synergy.backend.domain.member.entity.RoleType;

public interface AttendeeService {

	void addJobInfo(String email, JobInfoRequestDto request);

	void addJobInfoDetails(String email, JobInfoDetailsRequestDto request);

	MyInfoResponseDto getMyInformation(String identifier);

	AttendeeFullInfoResponseDto getAttendeeInfoDetail(Long attendeeId, String identifier, RoleType role);
}
