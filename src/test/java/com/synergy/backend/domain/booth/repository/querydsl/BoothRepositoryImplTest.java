package com.synergy.backend.domain.booth.repository.querydsl;

import com.synergy.backend.domain.booth.dto.BoothParticipationResponseDto;
import com.synergy.backend.domain.booth.repository.BoothRepository;
import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.conference.repository.ConferenceRepository;
import com.synergy.backend.global.config.QuerydslConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BoothRepositoryImplTest {

    @Autowired
    BoothRepository boothRepository;
    @Autowired
    ConferenceRepository conferenceRepository;

    @DisplayName("컨퍼런스 부스별로 참여한 사용자의 관심 직무에 대한 정보를 조회합니다.")
    @Test
    void searchBoothParticipation() {
        // given
        Conference savedConference = conferenceRepository.findById(1L)
                .orElseThrow(() -> new IllegalStateException("data-test.sql 이 실행되지 않았습니다."));

        // when
        List<BoothParticipationResponseDto> result = boothRepository.searchBoothParticipation(savedConference.getId());

        // then
        assertThat(result)
                .hasSize(2)
                .extracting("companyName", "companyType", "boothLocation", "boothNumber", "progressDate")
                .containsExactlyInAnyOrder(
                        tuple("CodeSphere", "YourCompanyType", "C HALL", "101C", LocalDate.of(3021, 10, 11)),
                        tuple("SkrrrCode", "BusinessCompanyType", "B HALL", "103B", LocalDate.of(3021, 10, 11))
                );


    }

}