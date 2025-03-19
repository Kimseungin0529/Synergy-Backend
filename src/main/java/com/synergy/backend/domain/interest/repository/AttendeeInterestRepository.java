package com.synergy.backend.domain.interest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.synergy.backend.domain.interest.entity.AttendeeInterest;

public interface AttendeeInterestRepository extends JpaRepository<AttendeeInterest, Long> {
}
