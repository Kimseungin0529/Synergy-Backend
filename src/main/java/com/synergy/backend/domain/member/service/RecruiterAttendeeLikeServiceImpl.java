package com.synergy.backend.domain.member.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.synergy.backend.domain.member.api.dto.resposne.LikedAttendeeResponseDto;
import com.synergy.backend.domain.member.api.dto.resposne.LikedRecruiterResponseDto;
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
public class RecruiterAttendeeLikeServiceImpl implements RecruiterAttendeeLikeService {

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
	@Override
	public List<LikedAttendeeResponseDto> getLikedAttendees(Long recruiterId) {
		List<RecruiterAttendeeLike> likes = recruiterAttendeeLikeRepository.findAllByRecruiterId(recruiterId);

		return likes.stream()
			.map(like -> LikedAttendeeResponseDto.from(like.getAttendee()))
			.toList();
	}

	@Transactional(readOnly = true)
	@Override
	public List<LikedRecruiterResponseDto> getLikedRecruiters(Long attendeeId) {
		List<RecruiterAttendeeLike> likes = recruiterAttendeeLikeRepository.findAllByAttendeeId(attendeeId);

		return likes.stream()
			.map(like -> LikedRecruiterResponseDto.from(like.getRecruiter()))
			.toList();
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
