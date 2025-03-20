package com.synergy.backend.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.Recruiter;
import com.synergy.backend.domain.member.entity.RecruiterAttendeeLike;
import com.synergy.backend.domain.member.exception.DuplicateLikeException;
import com.synergy.backend.domain.member.exception.NotFoundRecruiterException;
import com.synergy.backend.domain.member.exception.NotFoundUserException;
import com.synergy.backend.domain.member.repository.AttendeeRepository;
import com.synergy.backend.domain.member.repository.RecruiterAttendeeLikeRepository;
import com.synergy.backend.domain.member.repository.RecruiterRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecruiterLikeServiceImpl implements RecruiterLikeService {

	private final RecruiterAttendeeLikeRepository recruiterAttendeeLikeRepository;
	private final RecruiterRepository recruiterRepository;
	private final AttendeeRepository attendeeRepository;

	@Transactional
	@Override
	public void likeAttendee(Long recruiterId, Long attendeeId) {
		Recruiter recruiter = findRecruiterById(recruiterId);
		Attendee attendee = findAttendeeById(attendeeId);

		if (recruiterAttendeeLikeRepository.existsByRecruiterAndAttendee(recruiter, attendee)) {
			throw new DuplicateLikeException();
		}

		recruiterAttendeeLikeRepository.save(RecruiterAttendeeLike.of(recruiter, attendee));
	}

	@Transactional
	@Override
	public void unlikeAttendee(Long recruiterId, Long attendeeId) {
		Recruiter recruiter = findRecruiterById(recruiterId);
		Attendee attendee = findAttendeeById(attendeeId);

		recruiterAttendeeLikeRepository.deleteByRecruiterAndAttendee(recruiter, attendee);
	}

	@Transactional(readOnly = true)
	private Recruiter findRecruiterById(Long recruiterId) {
		return recruiterRepository.findById(recruiterId).orElseThrow(NotFoundRecruiterException::new);
	}

	@Transactional(readOnly = true)
	private Attendee findAttendeeById(Long attendeeId) {
		return attendeeRepository.findById(attendeeId).orElseThrow(NotFoundUserException::new);
	}
}
