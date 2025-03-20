package com.synergy.backend.domain.member.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.details.MembershipLevelType;

public interface AttendeeRepository extends JpaRepository<Attendee, Long> {
	Optional<Attendee> findByEmail(String email);

	Page<Attendee> findByMembershipLevelTypeOrderByTotalPointsDesc(MembershipLevelType membershipLevelType,
		Pageable pageable);

	Page<Attendee> findAllByOrderByTotalPointsDesc(Pageable pageable);
}
