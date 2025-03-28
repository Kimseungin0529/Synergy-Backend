package com.synergy.backend.domain.conference.repository;

import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.conference.entity.TimePeriod;
import com.synergy.backend.global.config.QuerydslConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@ActiveProfiles("test")
@Import(QuerydslConfig.class)
class ConferenceRepositoryTest {

    @Autowired
    ConferenceRepository conferenceRepository;

    @DisplayName("티켓 코드를 통해 컨퍼런스를 조회합니다.")
    @Test
    void findByTicketCode() {
        // given
        LocalDate startDate = LocalDate.of(3017, 2, 3);
        LocalDate endDate = LocalDate.of(3017, 2, 5);
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(18, 0);
        Conference conference = Conference.of("testName", TimePeriod.of(startDate, endDate, startTime, endTime), "organizer",
                "location", "position", "type");

        String ticketCode = "TEST_CODE";
        conference.addTicketCode(ticketCode);
        Conference savedConference = conferenceRepository.save(conference);
        // when
        Conference result = conferenceRepository.findByTicketCode(ticketCode)
                .orElseThrow(() -> new IllegalArgumentException("Ticket code not found"));
        // then
        assertThat(result.getTicketCode()).isEqualTo(ticketCode);
        assertThat(result.getId()).isEqualTo(savedConference.getId());
    }

}