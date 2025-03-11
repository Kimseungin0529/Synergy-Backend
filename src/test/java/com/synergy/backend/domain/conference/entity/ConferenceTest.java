package com.synergy.backend.domain.conference.entity;

import com.synergy.backend.domain.conference.exception.InvalidLocationException;
import com.synergy.backend.domain.conference.exception.InvalidNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static com.synergy.backend.domain.conference.exception.ErrorType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ConferenceTest {

    @DisplayName("컨퍼런스 정보를 등록합니다.")
    @Test
    void of() {
        // given
        String name = "컨퍼런스 제목";
        TimePeriod timePeriod = TimePeriod.of(LocalDateTime.of(2025, 4, 14, 9, 0), LocalDateTime.of(2025, 4, 15, 16, 0));
        String organizer = "김승진";
        String location = "컨퍼런스 위치";
        String type = "IT";
        // when
        Conference conference = Conference.of(name, timePeriod, organizer, location, type);
        // then
        assertThat(conference)
                .extracting("name", "organizer", "location", "type")
                .containsExactly(name, organizer, location, type);
    }

    @DisplayName("잘못된 컨퍼런스명 형식으로 등록하는 시나리오")
    @TestFactory
    Collection<DynamicTest> ofExceptionName() {
        // given
        TimePeriod timePeriod = TimePeriod.of(LocalDateTime.of(2025, 4, 14, 9, 0), LocalDateTime.of(2025, 4, 15, 16, 0));
        String location = "컨퍼런스 위치";
        String organizer = "김승진";
        String type = "산업";

        // when & then
        return List.of(
                DynamicTest.dynamicTest("컨퍼런스 제목은 공백으로 작성할 수 없습니다..", () -> {
                            //given
                            String name = "  ";
                            // when & then
                            assertThatThrownBy(() -> Conference.of(name, timePeriod, organizer, location, type))
                                    .hasMessage(_INVALID_CONFERENCE_NAME.getMessage())
                                    .isInstanceOf(InvalidNameException.class);
                        }
                ),
                DynamicTest.dynamicTest("컨퍼런스 제목은 30자 이내 입니다.", () -> {
                            //given
                            String name = "123456789/123456789/123456789/1";
                            // when & then
                            assertThatThrownBy(() -> Conference.of(name, timePeriod, organizer, location, type))
                                    .hasMessage(_INVALID_CONFERENCE_NAME.getMessage())
                                    .isInstanceOf(InvalidNameException.class);
                        }
                )
        );
    }

    @DisplayName("잘못된 주최자명 형식으로 등록하는 시나리오")
    @TestFactory
    Collection<DynamicTest> ofExceptionOrganizer() {
        // given
        TimePeriod timePeriod = TimePeriod.of(LocalDateTime.of(2025, 4, 14, 9, 0), LocalDateTime.of(2025, 4, 15, 16, 0));
        String name = "카카오 IT 컨퍼런스";
        String location = "컨퍼런스 위치";
        String type = "산업";
        // when & then
        return List.of(
                DynamicTest.dynamicTest("주최자명은 공백으로 작성할 수 없습니다.", () -> {
                            //given
                            String organizer = "  ";
                            // when & then
                            assertThatThrownBy(() -> Conference.of(name, timePeriod, organizer, location, type))
                                    .hasMessage(_INVALID_ORGANIZER.getMessage())
                                    .isInstanceOf(InvalidOrganizerException.class);
                        }
                ),
                DynamicTest.dynamicTest("주최자명은 10자 이내 입니다.", () -> {
                            //given
                            String organizer = "김 승진 수수깡, /";
                            // when & then
                            assertThatThrownBy(() -> Conference.of(name, timePeriod, organizer, location, type))
                                    .hasMessage(_INVALID_ORGANIZER.getMessage())
                                    .isInstanceOf(InvalidOrganizerException.class);
                        }
                )
        );
    }

    @DisplayName("잘못된 형식으로 위치명을 등록하는 시나리오")
    @TestFactory
    Collection<DynamicTest> ofExceptionLocation() {
        // given
        TimePeriod timePeriod = TimePeriod.of(LocalDateTime.of(2025, 4, 14, 9, 0), LocalDateTime.of(2025, 4, 15, 16, 0));
        String name = "카카오 IT 컨퍼런스";
        String organizer = "김승진";
        String type = "산업";
        // when & then
        return List.of(
                DynamicTest.dynamicTest("컨퍼런스 위치는 공백으로 작성할 수 없습니다..", () -> {
                            //given
                            String location = "  ";
                            // when & then
                            assertThatThrownBy(() -> Conference.of(name, timePeriod, organizer, location, type))
                                    .hasMessage(_INVALID_CONFERENCE_LOCATION.getMessage())
                                    .isInstanceOf(InvalidLocationException.class);
                        }
                ),
                DynamicTest.dynamicTest("컨퍼런스 위치는 100자 이내 입니다.", () -> {
                            //given
                            String location = "지구에 존재하는 동양 국가로 대한민국 서울특별시에 존재하는 어쩌구 저쩌구 어쩌구 저쩌구 어쩌구 저쩌구 어쩌구 저쩌구저쩌구저쩌구저쩌구저쩌구저쩌구저쩌구저쩌구 마포에 있는 커스텀 건물12";

                            // when & then
                            assertThatThrownBy(() -> Conference.of(name, timePeriod, organizer, location, type))
                                    .hasMessage(_INVALID_CONFERENCE_LOCATION.getMessage())
                                    .isInstanceOf(InvalidLocationException.class);
                        }
                )
        );
    }

    @DisplayName("잘못된 유형 형식으로 등록하는 시나리오")
    @TestFactory
    Collection<DynamicTest> ofExceptionType() {
        // given
        TimePeriod timePeriod = TimePeriod.of(LocalDateTime.of(2025, 4, 14, 9, 0), LocalDateTime.of(2025, 4, 15, 16, 0));
        String name = "카카오 IT 컨퍼런스";
        String organizer = "김승진";
        String location = "서울 마포대로 지하 축제 공간";
        // when & then
        return List.of(
                DynamicTest.dynamicTest("유형은 공백으로 작성할 수 없습니다..", () -> {
                            //given
                            String type = "  ";
                            // when & then
                            assertThatThrownBy(() -> Conference.of(name, timePeriod, organizer, location, type))
                                    .hasMessage(_INVALID_COMMON.getMessage())
                                    .isInstanceOf(InvalidCommonException.class);
                        }
                ),
                DynamicTest.dynamicTest("유형은 50자 이내 입니다.", () -> {
                            //given
                            String type = "  ";
                            // when & then
                            assertThatThrownBy(() -> Conference.of(name, timePeriod, organizer, location, type))
                                    .hasMessage(_INVALID_COMMON.getMessage())
                                    .isInstanceOf(InvalidCommonException.class);
                        }
                )
        );
    }
}