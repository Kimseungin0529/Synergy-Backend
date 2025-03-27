package com.synergy.backend.domain.session.entity;

import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.conference.entity.TimePeriod;
import com.synergy.backend.domain.session.dto.sessionDto.SessionReqDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SessionTest {

//    @DisplayName("세션 정보를 등록합니다.")
//    @Test
//    void of() {
//        // given
//        LocalDate progressDate = LocalDate.of(2025, 3, 29);
//        LocalDateTime startDate = LocalDateTime.of(progressDate, LocalTime.NOON);
//        LocalDateTime endDate = LocalDateTime.of(progressDate, LocalTime.of(14, 30));
//
//        Conference conference = Conference.of("컨퍼런스 제목",
//                TimePeriod.of(LocalDateTime.of(2025, 3, 28, 9, 0),
//                        LocalDateTime.of(2025, 3, 31, 18, 0)),
//                "박정곤", "컨퍼런스 위치", "IT");
//
//        SessionReqDto dto = new SessionReqDto("title", "speaker", "speakerPosition",
//                progressDate, startDate, endDate, "description1", 200);
//
//        // when
//        Session session = Session.of(dto, "secretCode", conference);
//
//        // then
//        assertThat(session).extracting(
//                "title", "speaker", "speakerPosition", "progressDate", "startTime", "endTime", "description", "maximum", "secretCode", "conference"
//        ).containsExactly(
//                "title", "speaker", "speakerPosition", progressDate, startDate, endDate,
//                "description1", 200, "secretCode", conference);
//    }

}