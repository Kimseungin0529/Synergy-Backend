package com.synergy.backend.domain.job;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JobCategoryRepository extends JpaRepository<JobCategory, Long> {
	Optional<JobCategory> findByCode(Integer code);
}
