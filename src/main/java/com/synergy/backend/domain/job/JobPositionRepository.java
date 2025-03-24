package com.synergy.backend.domain.job;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPositionRepository extends JpaRepository<JobPosition, Long> {
	Optional<JobPosition> findByCode(Integer code);
}
