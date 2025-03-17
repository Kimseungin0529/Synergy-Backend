package com.synergy.backend.domain.session.repository;

import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.session.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findAllByConferenceOrderByStartTime(Conference conference);

    Optional<Session> findBySecretCode(String secretCode);
}
