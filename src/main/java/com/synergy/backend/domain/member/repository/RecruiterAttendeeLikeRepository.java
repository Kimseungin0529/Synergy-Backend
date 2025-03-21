package com.synergy.backend.domain.member.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.Recruiter;
import com.synergy.backend.domain.member.entity.RecruiterAttendeeLike;

public interface RecruiterAttendeeLikeRepository extends JpaRepository<RecruiterAttendeeLike, Long> {
	Optional<RecruiterAttendeeLike> findByRecruiterAndAttendee(Recruiter recruiter, Attendee attendee);

	boolean existsByRecruiterAndAttendee(Recruiter recruiter, Attendee attendee);

	void deleteByRecruiterAndAttendee(Recruiter recruiter, Attendee attendee);

	List<RecruiterAttendeeLike> findAllByRecruiterId(Long recruiterId);

	List<RecruiterAttendeeLike> findAllByAttendeeId(Long attendeeId);
}
