package com.synergy.backend.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.synergy.backend.domain.member.entity.Recruiter;

public interface RecruiterRepository extends JpaRepository<Recruiter, Long> {
}
