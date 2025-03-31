package com.synergy.backend.domain.point.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.synergy.backend.domain.point.entity.Point;

public interface PointRepository extends JpaRepository<Point, Long> {
	List<Point> findByAttendeeIdOrderByCreatedTimeDesc(Long attendeeId);

	@Query("SELECT p FROM Point p " +
		"WHERE p.attendee.id = :attendeeId " +
		"ORDER BY p.createdTime DESC " +
		"LIMIT 3")
	List<Point> findRecentPointsByAttendeeId(@Param("attendeeId") Long attendeeId);
}
