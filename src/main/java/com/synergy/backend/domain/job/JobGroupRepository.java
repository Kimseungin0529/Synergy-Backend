package com.synergy.backend.domain.job;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JobGroupRepository extends JpaRepository<JobGroup, Long> {
	Optional<JobGroup> findByCode(Integer code);

}
