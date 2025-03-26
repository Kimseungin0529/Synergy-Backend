package com.synergy.backend.domain.conference.entity;

import com.synergy.backend.domain.conference.exception.InvalidTimePeriodException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class TimePeriod {

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    private TimePeriod(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static TimePeriod of(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        // 시작 날짜 <= 종료 날짜
        // 시작 시간 < 종료 시간
        if(isNotValidDateBetween(startDate, endDate) || isNotValidTimeBetween(startTime, endTime)) {
            throw new InvalidTimePeriodException();
        }
        return new TimePeriod(startDate, endDate, startTime, endTime);
    }

    private static boolean isNotValidTimeBetween(LocalTime startTime, LocalTime endTime) {
        return startTime.isAfter(endTime) || startTime.equals(endTime);
    }

    private static boolean isNotValidDateBetween(LocalDate startDate, LocalDate endDate) {
        return startDate.isAfter(endDate);
    }


}
