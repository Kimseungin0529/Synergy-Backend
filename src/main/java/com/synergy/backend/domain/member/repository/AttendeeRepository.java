package com.synergy.backend.domain.member.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.synergy.backend.domain.member.api.dto.response.AttendeePointRankingResponseDto;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.details.MembershipLevelType;

public interface AttendeeRepository extends JpaRepository<Attendee, Long>, AttendeeRepositoryCustom {
	Optional<Attendee> findByEmail(String email);

	Page<Attendee> findByMembershipLevelTypeOrderByTotalPointsDesc(MembershipLevelType membershipLevelType,
		Pageable pageable);

	Page<Attendee> findByConferenceIdOrderByTotalPointsDesc(Long conferenceId, Pageable pageable);

	@Query("select a from Attendee a join fetch a.currentJobPosition " +
		"join fetch a.currentJobGroup where a.id = :id")
	Optional<Attendee> findAttendeeBy(@Param("id") Long attendeeId);

	Long countAttendeeByConferenceId(Long conferenceId);

	@Query(
		"SELECT new com.synergy.backend.domain.member.api.dto.response.AttendeePointRankingResponseDto(a.id, a.name, a.totalPoints) "
			+
			"FROM Attendee a WHERE a.conference.id = :conferenceId ORDER BY a.totalPoints DESC"
	)
	Page<AttendeePointRankingResponseDto> findTopAttendeeRankingsDtoByConferenceId(
		@Param("conferenceId") Long conferenceId,
		Pageable pageable
	);
}
