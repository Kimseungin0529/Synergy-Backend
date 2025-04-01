package com.synergy.backend.domain.session.entity;

import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.session.dto.sessionDto.SessionReqDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;

class SessionTest {

    @DisplayName("세션 정보를 등록합니다.")
    @Test
    void of() {
        // given
        LocalDate progressDate = LocalDate.of(2025, 4, 1);
        LocalDateTime startDate = LocalDateTime.of(progressDate, LocalTime.NOON);
        LocalDateTime endDate = LocalDateTime.of(progressDate, LocalTime.of(14, 30));

        Conference conference = mock(Conference.class);

        SessionReqDto dto = new SessionReqDto("title", "speaker", "speakerPosition",
                progressDate, startDate, endDate, "description1", 200);

        // when
        Session session = Session.of(dto, "secretCode", conference);

        // then
        assertThat(session).extracting(
                "title", "speaker", "speakerPosition", "progressDate", "startTime", "endTime", "description", "maximum", "secretCode", "conference"
        ).containsExactly(
                "title", "speaker", "speakerPosition", progressDate, startDate, endDate,
                "description1", 200, "secretCode", conference);
    }

    @DisplayName("세션 정보를 업데이트 합니다.")
    @Test
    void updateSession(){
     //given
        LocalDate progressDate = LocalDate.of(2025, 4, 1);
        LocalDateTime startDate = LocalDateTime.of(progressDate, LocalTime.NOON);
        LocalDateTime endDate = LocalDateTime.of(progressDate, LocalTime.of(14, 30));

        Conference conference = mock(Conference.class);

        SessionReqDto dto = new SessionReqDto("title", "speaker", "speakerPosition",
                progressDate, startDate, endDate, "description1", 200);
        SessionReqDto updateDto = new SessionReqDto("updateTitle", "updatedSpeaker", "speakerPosition",
                progressDate, startDate, endDate, "new Description", 250);

     //when
        Session session = Session.of(dto, "secretCode", conference);
        session.updateSession(updateDto);

     //then
        assertThat(session).extracting(
                "title", "speaker", "speakerPosition", "description", "maximum"
        ).containsExactly(
                "updateTitle", "updatedSpeaker", "speakerPosition", "new Description", 250
        );
    }

}