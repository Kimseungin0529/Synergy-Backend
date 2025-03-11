package com.synergy.backend.domain.conference.service;

import com.synergy.backend.domain.conference.dto.requset.ConferenceCreateRequest;
import com.synergy.backend.domain.conference.dto.requset.ConferenceUpdateRequest;
import com.synergy.backend.domain.conference.dto.response.ConferenceCreateResponse;
import com.synergy.backend.domain.conference.dto.response.ConferenceUpdateResponse;
import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.conference.entity.TimePeriod;
import com.synergy.backend.domain.conference.repository.ConferenceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
class ConferenceServiceImplTest {
    @Autowired
    ConferenceService conferenceService;
    @Autowired
    ConferenceRepository conferenceRepository;

    @DisplayName("")
    @Test
    void registerConference() {
        // given
        ConferenceCreateRequest request = new ConferenceCreateRequest(
                "컨퍼런스명",
                LocalDateTime.of(2025,5,10,9,0),
                LocalDateTime.of(2025,5,11,18,0),
                "부천시 오정구 고강동 311-25 1층",
                "김승진",
                "IT"
        );
        // when
        ConferenceCreateResponse result = conferenceService.registerConference(request);
        // then
        assertThat(result)
                .isNotNull()
                .extracting(ConferenceCreateResponse::id)
                .isNotNull();
    }

    @DisplayName("컨퍼런스 정보 변경하는 시나리오")
    @TestFactory
    Collection<DynamicTest> updateConference() {
        // given
        String name = "컨퍼런스명";
        LocalDateTime startTime = LocalDateTime.of(2024, 3, 5, 13, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 3, 5, 20, 0);
        String location = "뉴욕 뉴져지";
        String organizer = "김승진";
        String type = "IT";

        Conference savedConference = conferenceRepository.save(
                Conference.of(name, TimePeriod.of(startTime, endTime), organizer, location, type)
        );
        Long conferenceId = savedConference.getId();

        String updatedName = "카카오 개발자로 살아남기";
        LocalDateTime updatedStartTime = LocalDateTime.of(2024, 3, 5, 15, 0);
        LocalDateTime updatedEndTime = LocalDateTime.of(2024, 3, 5, 19, 0);
        String updatedLocation = "서울 여의도 국회 1층";
        String updatedOrganizer = "뽀로로";
        String updatedType = "산업";
        // when & then
        return List.of(
                DynamicTest.dynamicTest("컨퍼런스명을 수정합니다.", () -> {
                        //given

                        ConferenceUpdateRequest request = new ConferenceUpdateRequest(
                                updatedName, null, null, null, null, null
                        );
                        //when
                        ConferenceUpdateResponse result = conferenceService.updateConference(conferenceId, request);
                        //then
                            assertThat(result)
                                    .extracting(ConferenceUpdateResponse::name,
                                            ConferenceUpdateResponse::startTime,
                                            ConferenceUpdateResponse::endTime,
                                            ConferenceUpdateResponse::location,
                                            ConferenceUpdateResponse::organizer,
                                            ConferenceUpdateResponse::type)
                                    .containsExactly(updatedName,
                                            savedConference.getPeriod().getStartDateTime(),
                                            savedConference.getPeriod().getEndDateTime(),
                                            savedConference.getLocation(),
                                            savedConference.getOrganizer(),
                                            savedConference.getType()
                                    );
                    }
                ),
                DynamicTest.dynamicTest("위치 정보를 수정합니다.", () -> {
                            //given
                            ConferenceUpdateRequest request = new ConferenceUpdateRequest(
                                    null, null, null, updatedLocation, null, null
                            );
                            //when
                            ConferenceUpdateResponse result = conferenceService.updateConference(conferenceId, request);
                            //then
                            assertThat(result)
                                    .extracting(ConferenceUpdateResponse::name,
                                            ConferenceUpdateResponse::startTime,
                                            ConferenceUpdateResponse::endTime,
                                            ConferenceUpdateResponse::location,
                                            ConferenceUpdateResponse::organizer,
                                            ConferenceUpdateResponse::type)
                                    .containsExactly(
                                            updatedName,
                                            savedConference.getPeriod().getStartDateTime(),
                                            savedConference.getPeriod().getEndDateTime(),
                                            updatedLocation,
                                            savedConference.getOrganizer(),
                                            savedConference.getType()
                                    );
                        }
                ),
                DynamicTest.dynamicTest("컨퍼런스 기간을 수정합니다.", () -> {
                            //given
                            ConferenceUpdateRequest request = new ConferenceUpdateRequest(
                                    null, updatedStartTime, updatedEndTime, null, null, null
                            );
                            //when
                            ConferenceUpdateResponse result = conferenceService.updateConference(conferenceId, request);
                            //then
                            assertThat(result)
                                    .extracting(ConferenceUpdateResponse::name,
                                            ConferenceUpdateResponse::startTime,
                                            ConferenceUpdateResponse::endTime,
                                            ConferenceUpdateResponse::location,
                                            ConferenceUpdateResponse::organizer,
                                            ConferenceUpdateResponse::type
                                    )
                                    .containsExactly(
                                            updatedName,
                                            updatedStartTime,
                                            updatedEndTime,
                                            updatedLocation,
                                            savedConference.getOrganizer(),
                                            savedConference.getType()
                                    );
                        }
                ),
                DynamicTest.dynamicTest("주최자를 수정합니다.", () -> {
                            //given
                            ConferenceUpdateRequest request = new ConferenceUpdateRequest(
                                    null, null, null, null, updatedOrganizer, null
                            );
                            //when
                            ConferenceUpdateResponse result = conferenceService.updateConference(conferenceId, request);
                            //then
                            assertThat(result)
                                    .extracting(ConferenceUpdateResponse::name,
                                            ConferenceUpdateResponse::startTime,
                                            ConferenceUpdateResponse::endTime,
                                            ConferenceUpdateResponse::location,
                                            ConferenceUpdateResponse::organizer,
                                            ConferenceUpdateResponse::type
                                    )
                                    .containsExactly(
                                            updatedName,
                                            updatedStartTime,
                                            updatedEndTime,
                                            updatedLocation,
                                            savedConference.getOrganizer(),
                                            savedConference.getType()
                                    );
                        }
                ),
                DynamicTest.dynamicTest("유형을 수정합니다.", () -> {
                            //given
                            ConferenceUpdateRequest request = new ConferenceUpdateRequest(
                                    null, null, null, null, null, type
                            );
                            //when
                            ConferenceUpdateResponse result = conferenceService.updateConference(conferenceId, request);
                            //then
                            assertThat(result)
                                    .extracting(ConferenceUpdateResponse::name,
                                            ConferenceUpdateResponse::startTime,
                                            ConferenceUpdateResponse::endTime,
                                            ConferenceUpdateResponse::location,
                                            ConferenceUpdateResponse::organizer,
                                            ConferenceUpdateResponse::type
                                    )
                                    .containsExactly(
                                            updatedName,
                                            updatedStartTime,
                                            updatedEndTime,
                                            updatedLocation,
                                            savedConference.getOrganizer(),
                                            savedConference.getType()
                                    );
                        }
                )
        );
    }

}