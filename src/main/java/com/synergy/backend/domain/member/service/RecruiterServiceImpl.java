package com.synergy.backend.domain.member.service;

import com.synergy.backend.domain.member.api.dto.AttendeeFilterRequest;
import com.synergy.backend.domain.member.api.dto.AttendeeListResponse;
import com.synergy.backend.domain.member.api.dto.AttendeeSimpleResponseDto;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.exception.ForbiddenAccessAttendee;
import com.synergy.backend.domain.member.exception.NotFoundUserException;
import com.synergy.backend.domain.member.repository.AttendeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.synergy.backend.domain.member.api.dto.resposne.RecruiterMyInfoResponseDto;
import com.synergy.backend.domain.member.entity.Recruiter;
import com.synergy.backend.domain.member.exception.NotFoundRecruiterException;
import com.synergy.backend.domain.member.repository.RecruiterRepository;

import lombok.RequiredArgsConstructor;

@Service @Slf4j
@Transactional
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
			throw new ForbiddenAccessAttendee();
		}
		return AttendeeDetailResponse.of(findAttendee);
	}

	@Transactional(readOnly = true)
	@Override
	public AttendeeListResponse getAttendeesBy(Pageable pageable, Long recruiterId, AttendeeFilterRequest requestCondition) {
		log.info(requestCondition.toString());
		Page<AttendeeSimpleResponseDto> pageAttendees = attendeeRepository.searchPageAttendeesBy(pageable, recruiterId, requestCondition);

		return AttendeeListResponse.from(pageAttendees);
	}

	private Recruiter findRecruiterById(Long id) {
		return recruiterRepository.findById(id).orElseThrow(NotFoundRecruiterException::new);
	}
}
