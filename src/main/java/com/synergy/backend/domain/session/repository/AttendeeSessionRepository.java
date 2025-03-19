package com.synergy.backend.domain.session.repository;

import com.synergy.backend.domain.session.entity.AttendeeSession;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface AttendeeSessionRepository extends JpaRepository<AttendeeSession, Long> {


    Optional<AttendeeSession> findBySessionIdAndAttendeeId(Long sessionId, Long attendeeId);
}
