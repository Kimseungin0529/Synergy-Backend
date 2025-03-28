package com.synergy.backend.domain.member.service;

import org.springframework.web.multipart.MultipartFile;

import com.synergy.backend.domain.member.api.dto.request.JobInfoDetailsRequestDto;
import com.synergy.backend.domain.member.api.dto.request.JobInfoRequestDto;
import com.synergy.backend.domain.member.api.dto.resposne.AttendeeFullInfoResponseDto;
import com.synergy.backend.domain.member.api.dto.resposne.MyInfoResponseDto;
import com.synergy.backend.domain.member.api.dto.resposne.ProfileImageUpdatedResponseDto;
import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.global.security.CustomUserDetails;

public interface AttendeeService {

	void addJobInfo(String email, JobInfoRequestDto request);

	void addJobInfoDetails(String email, JobInfoDetailsRequestDto request, MultipartFile profileImage);

	MyInfoResponseDto getMyInformation(String identifier);

	AttendeeFullInfoResponseDto getAttendeeInfoDetail(Long attendeeId, String identifier, RoleType role);

	ProfileImageUpdatedResponseDto updateProfileImage(CustomUserDetails userDetails, MultipartFile profileImage);
}
