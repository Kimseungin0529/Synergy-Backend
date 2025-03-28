package com.synergy.backend.domain.booth.repository;

import com.synergy.backend.domain.booth.entity.Booth;
import com.synergy.backend.domain.conference.repository.ConferenceRepository;
import com.synergy.backend.global.config.QuerydslConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import(QuerydslConfig.class)
class BoothRepositoryTest {
    @Autowired
    BoothRepository boothRepository;
    @Autowired
    ConferenceRepository conferenceRepository;

    /*@DisplayName("부스와 컨퍼런스의 ID를 통해 부스를 조회합니다.")
    @Test // data-test.sql 에 종속적이고 도메인 구조가 테스트 구조에 적합하지 않음. data-test.sql 과 강결합하여 테스트하기 어려운 구조라 판단
    void findByIdAndConferenceId() {
        // given
        LocalDate startDate = LocalDate.of(3030, 2, 2);
        LocalTime startTime = LocalTime.of(9, 0);
        LocalDate endDate = LocalDate.of(3030, 2, 4);
        LocalTime endTime = LocalTime.of(18, 0);
        Conference conference = Conference.of("test", TimePeriod.of(startDate, endDate, startTime, endTime), "김승진", "강남 중심", "야외", "번개 모임");
        Booth booth = new Booth("test", "test1", "test1", "test1", LocalDate.of(3030, 2, 2), "test1", conference);

        Conference savedConference = conferenceRepository.save(conference);
        Booth savedBooth = boothRepository.save(booth);

        // when
        Booth result = boothRepository.findByIdAndConferenceId(savedConference.getId(), savedBooth.getId())
                .orElseThrow(() -> new RuntimeException("Booth not found"));
        // then
        assertThat(result)
                .isNotNull()
                .extracting("companyName", "companyType", "boothLocation", "boothNumber")
                .containsExactly("test", "test1", "test1", "test1");
    }*/

    @DisplayName("부스와 컨퍼런스의 ID를 통해 부스를 조회합니다.")
    @Test
    void findByIdAndConferenceId() {
        // given & when
        Booth result = boothRepository.findByIdAndConferenceId(1L, 1L)
                .orElseThrow(() -> new RuntimeException("Booth not found"));
        // then
        assertThat(result)
                .isNotNull()
                .extracting("companyName", "companyType", "boothLocation", "boothNumber")
                .containsExactly("CodeSphere", "YourCompanyType", "C HALL", "101C");
    }


}