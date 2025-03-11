package com.synergy.backend.domain.booth.repository;

import com.synergy.backend.domain.booth.entity.AttendeeBooth;
import com.synergy.backend.domain.booth.entity.Booth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AttendeeBoothRepository extends JpaRepository<AttendeeBooth, Long> {
    List<AttendeeBooth> findByBooth(Booth booth);
    boolean existsByBoothIdAndAttendeeId(Long boothId, Long attendeeId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("DELETE FROM AttendeeBooth ab WHERE ab.booth = :booth")
    void deleteByBooth(Booth booth);
}
