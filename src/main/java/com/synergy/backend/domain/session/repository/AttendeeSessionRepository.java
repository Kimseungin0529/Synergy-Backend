package com.synergy.backend.domain.session.repository;

import com.synergy.backend.domain.session.entity.AttendeeSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendeeSessionRepository extends JpaRepository<AttendeeSession, Long> {

    List<AttendeeSession> findAllBySession(Long attendeeId);
}
