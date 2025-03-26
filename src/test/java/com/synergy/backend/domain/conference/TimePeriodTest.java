package com.synergy.backend.domain.conference;

import com.synergy.backend.domain.conference.entity.TimePeriod;
import com.synergy.backend.domain.conference.exception.InvalidTimePeriodException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TimePeriodTest {

    @DisplayName("기간을 설정합니다.")
    @Test
    void of() {
        // given
        //LocalDate startDate = LocalDateTime.of(2020, 3, 20, 9, 0);
        //LocalDate endDate = LocalDateTime.of(2020, 3, 21, 18, 0);

        LocalDate startDate = LocalDate.of(2020, 3, 20);
        LocalDate endDate = LocalDate.of(2020, 3, 21);
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(18, 0);
        // when
        TimePeriod timePeriod = TimePeriod.of(startDate, endDate, startTime, endTime);
        // then
        assertThat(timePeriod)
                .extracting("startDate", "endDate", "startTime", "endTime")
                .containsExactly(
                        LocalDate.of(2020, 3, 20),
                        LocalDate.of(2020, 3, 21),
                        LocalTime.of(9, 0),
                        LocalTime.of(18, 0)
                );


    }

    @DisplayName("시작 날짜는 종료 날짜보다 빠릅니다.")
    @Test
    void ofException() {
        // given
        LocalDate startDate = LocalDate.of(2020, 3, 20);
        LocalDate endDate = LocalDate.of(2020, 3, 20);

        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(9, 0);
        // when & then
        assertThatThrownBy(() -> TimePeriod.of(startDate, endDate, startTime, endTime))
                .hasMessage("날짜 및 시간 설정이 올바르지 않습니다.")
                .isInstanceOf(InvalidTimePeriodException.class);
    }


}