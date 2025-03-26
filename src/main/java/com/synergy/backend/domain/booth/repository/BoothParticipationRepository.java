package com.synergy.backend.domain.booth.repository;

import com.synergy.backend.domain.booth.dto.InterestParticipationDto;
import com.synergy.backend.domain.booth.entity.BoothParticipation;
import com.synergy.backend.domain.booth.entity.Booth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoothParticipationRepository extends JpaRepository<BoothParticipation, Long> {

    List<BoothParticipation> findByBooth(Booth booth);

    boolean existsByBoothIdAndAttendeeId(Long boothId, Long attendeeId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM BoothParticipation bp WHERE bp.booth = :booth")
    void deleteByBooth(@Param("booth") Booth booth);

    @Query("SELECT new com.synergy.backend.domain.booth.dto.InterestParticipationDto(i.name, COUNT(bp)) " +
            "FROM BoothParticipation bp " +
            "JOIN bp.attendee a " +
            "JOIN a.attendeeInterests ai " +
            "JOIN ai.interest i " +
            "WHERE bp.booth.id = :boothId " +
            "GROUP BY i.name")
    List<InterestParticipationDto> findParticipationCountByInterest(@Param("boothId") Long boothId);

}
