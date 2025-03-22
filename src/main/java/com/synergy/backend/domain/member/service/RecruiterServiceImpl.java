package com.synergy.backend.domain.member.service;

import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.exception.NotFoundUserException;
import com.synergy.backend.domain.member.repository.AttendeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.synergy.backend.domain.member.api.dto.resposne.RecruiterMyInfoResponseDto;
import com.synergy.backend.domain.member.entity.Recruiter;
import com.synergy.backend.domain.member.exception.NotFoundRecruiterException;
import com.synergy.backend.domain.member.repository.RecruiterRepository;

import lombok.RequiredArgsConstructor;

@Service @Slf4j
@RequiredArgsConstructor
public class RecruiterServiceImpl implements RecruiterService {

	private final RecruiterRepository recruiterRepository;
	private final AttendeeRepository attendeeRepository;

	@Transactional(readOnly = true)
	@Override
	public RecruiterMyInfoResponseDto getMyInformation(Long id) {
		Recruiter recruiter = findRecruiterById(id);
		return RecruiterMyInfoResponseDto.from(recruiter);
	}

	@Transactional(readOnly = true)
	@Override
	public AttendeeDetailResponse getAttendeeFrom(Long attendeeId) {
		Attendee findAttendee = attendeeRepository.findAttendeeBy(attendeeId).orElseThrow(NotFoundUserException::new);
		if(!findAttendee.getIsHiringInterested()) {
			throw new IllegalArgumentException("참가자는 채용을 희망하지 않습니다.");
		}
		return AttendeeDetailResponse.of(findAttendee);
	}

	private Recruiter findRecruiterById(Long id) {
		return recruiterRepository.findById(id).orElseThrow(NotFoundRecruiterException::new);
	}
}
