package com.synergy.backend.domain.point.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.synergy.backend.domain.point.entity.Point;

public interface PointRepository extends JpaRepository<Point, Long> {
	List<Point> findByAttendeeIdOrderByCreatedTimeDesc(Long attendeeId);
}
