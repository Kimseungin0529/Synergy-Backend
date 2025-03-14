package com.synergy.backend.domain.interest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.synergy.backend.domain.interest.entity.Interest;

public interface InterestRepository extends JpaRepository<Interest, Long> {
}
