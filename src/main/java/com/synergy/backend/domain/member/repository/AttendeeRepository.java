package com.synergy.backend.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.synergy.backend.domain.member.entity.Attendee;

public interface AttendeeRepository extends JpaRepository<Attendee, Long> {
}
