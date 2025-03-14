package com.synergy.backend.domain.interest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.synergy.backend.domain.interest.entity.MemberInterest;

public interface MemberInterestRepository extends JpaRepository<MemberInterest, Long> {
}
