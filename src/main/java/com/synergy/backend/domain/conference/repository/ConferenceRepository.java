package com.synergy.backend.domain.conference.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.synergy.backend.domain.conference.entity.Conference;

public interface ConferenceRepository extends JpaRepository<Conference, Long> {
	Optional<Conference> findByTicketCode(String ticketCode);
}
