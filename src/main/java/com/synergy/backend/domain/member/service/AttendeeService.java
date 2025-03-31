package com.synergy.backend.domain.member.service;

import org.springframework.web.multipart.MultipartFile;

import com.synergy.backend.domain.member.api.dto.request.JobInfoDetailsRequestDto;
import com.synergy.backend.domain.member.api.dto.request.JobInfoRequestDto;
import com.synergy.backend.domain.member.api.dto.response.AttendeeFullInfoResponseDto;
import com.synergy.backend.domain.member.api.dto.response.MyInfoResponseDto;
import com.synergy.backend.domain.member.api.dto.response.ProfileImageUpdatedResponseDto;
import com.synergy.backend.domain.member.entity.RoleType;

public interface AttendeeService {

	void addJobInfo(Long id, JobInfoRequestDto request);

	void addJobInfoDetails(Long id, JobInfoDetailsRequestDto request, MultipartFile profileImage);

	MyInfoResponseDto getMyInformation(Long id);

	AttendeeFullInfoResponseDto getAttendeeInfoDetail(Long attendeeId, Long viewerId, RoleType role);

	ProfileImageUpdatedResponseDto updateProfileImage(Long id, MultipartFile profileImage);
}
