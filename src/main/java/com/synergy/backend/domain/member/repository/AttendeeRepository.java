package com.synergy.backend.domain.member.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.details.MembershipLevelType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AttendeeRepository extends JpaRepository<Attendee, Long>, AttendeeRepositoryCustom {
	Optional<Attendee> findByEmail(String email);

	Page<Attendee> findByMembershipLevelTypeOrderByTotalPointsDesc(MembershipLevelType membershipLevelType,
		Pageable pageable);

	Page<Attendee> findAllByOrderByTotalPointsDesc(Pageable pageable);

	@Query("select a from Attendee a join fetch a.currentJobCategory " +
			"join fetch a.currentOccupationCategory where a.id = :id")
    Optional<Attendee> findAttendeeBy(@Param("id") Long attendeeId);
}
