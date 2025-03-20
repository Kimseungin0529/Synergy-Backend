package com.synergy.backend.domain.interest.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.synergy.backend.domain.interest.entity.Interest;

public interface InterestRepository extends JpaRepository<Interest, Long> {
	List<Interest> findAllByCodeIn(Set<Integer> interestCodes);
}
