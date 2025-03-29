package com.synergy.backend.domain.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.synergy.backend.domain.member.entity.RecruiterAttendeeLike;

public interface RecruiterAttendeeLikeRepository extends JpaRepository<RecruiterAttendeeLike, Long> {

	@Query("select count(ral) > 0 from RecruiterAttendeeLike ral where ral.recruiter.id = :recruiterId and ral.attendee.id = :attendeeId")
	boolean existsLike(@Param("recruiterId") Long recruiterId, @Param("attendeeId") Long attendeeId);

	@Modifying
	@Query("delete from RecruiterAttendeeLike ral where ral.recruiter.id = :recruiterId and ral.attendee.id = :attendeeId")
	void deleteByRecruiterIdAndAttendeeId(@Param("recruiterId") Long recruiterId, @Param("attendeeId") Long attendeeId);

	List<RecruiterAttendeeLike> findAllByRecruiterId(Long recruiterId);

	List<RecruiterAttendeeLike> findAllByAttendeeId(Long attendeeId);

}
