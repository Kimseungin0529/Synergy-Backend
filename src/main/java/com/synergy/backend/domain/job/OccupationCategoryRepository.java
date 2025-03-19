package com.synergy.backend.domain.job;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OccupationCategoryRepository extends JpaRepository<OccupationCategory, Long> {
	Optional<OccupationCategory> findByCode(Integer code);

}
