package com.synergy.backend.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.synergy.backend.domain.member.entity.Attendee;

public interface AttendeeRepository extends JpaRepository<Attendee, Long> {
	Optional<Attendee> findByEmail(String email);
}
