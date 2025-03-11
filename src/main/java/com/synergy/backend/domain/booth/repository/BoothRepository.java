package com.synergy.backend.domain.booth.repository;

import com.synergy.backend.domain.booth.entity.Booth;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoothRepository extends JpaRepository<Booth, Long> {
    Optional<Booth> findByIdAndConferenceId(Long id, Long conferenceId);
    Page<Booth> findAllByConferenceId(Long conferenceId, Pageable pageable);
}