package com.synergy.backend.domain.conference.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.synergy.backend.domain.conference.dto.requset.ConferenceCreateRequest;
import com.synergy.backend.domain.conference.dto.requset.ConferenceUpdateRequest;
import com.synergy.backend.domain.conference.dto.response.ConferenceAttendeeInfoResDto;
import com.synergy.backend.domain.conference.dto.response.ConferenceCreateResponse;
import com.synergy.backend.domain.conference.dto.response.ConferenceUpdateResponse;
import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.conference.entity.TimePeriod;
import com.synergy.backend.domain.conference.repository.ConferenceRepository;
import com.synergy.backend.domain.member.entity.Admin;
import com.synergy.backend.domain.member.repository.AdminRepository;
import com.synergy.backend.domain.member.repository.AttendeeRepository;
import com.synergy.backend.module.IntegrationSupportTest;

class ConferenceServiceImplTest extends IntegrationSupportTest {
	@Autowired
	ConferenceService conferenceService;
	@Autowired
	ConferenceRepository conferenceRepository;
	@Autowired
	AdminRepository adminRepository;
	@Autowired
	AttendeeRepository attendeeRepository;

	@DisplayName("관리자가 컨퍼런스를 등록합니다.")
	@Test
	void registerConference() {
		// given
		String identifier = "CODE1234";
		Admin admin = Admin.of(identifier);
		ConferenceCreateRequest request = new ConferenceCreateRequest(
			"컨퍼런스명",
			"홍길동",
			LocalDate.of(3022, 6, 15),
			LocalTime.of(13, 0),
			LocalDate.of(3022, 6, 18),
			LocalTime.of(18, 0),
			"부천시 오정구 고강동 311-25 1층",
			"로비",
			"IT"
		);
		adminRepository.save(admin);
		// when
		ConferenceCreateResponse result = conferenceService.registerConference(identifier, request);
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
		String organizer = "김승진";
		LocalDate startDate = LocalDate.of(3024, 6, 15);
		LocalTime startTime = LocalTime.of(13, 0);
		LocalDate endDate = LocalDate.of(3024, 6, 18);
		LocalTime endTime = LocalTime.of(18, 0);
		String location = "뉴욕 뉴져지";
		String position = "중심 로비";
		String type = "IT";

		Conference savedConference = conferenceRepository.save(
			Conference.of(name, TimePeriod.of(startDate, endDate, startTime, endTime), organizer, location, position,
				type)
		);

		String identifier = "AUTH1";
		Admin admin = Admin.of(identifier);
		admin.addConference(savedConference);
		adminRepository.save(admin);

		Long conferenceId = savedConference.getId();

		String updatedName = "카카오 개발자로 살아남기";
		LocalDate updatedStartDate = LocalDate.of(3024, 7, 15);
		LocalTime updatedStartTime = LocalTime.of(12, 0);
		LocalDate updatedEndDate = LocalDate.of(3024, 7, 18);
		LocalTime updatedEndTime = LocalTime.of(20, 0);
		String updatedLocation = "서울 여의도 국회 1층";
		String updatedOrganizer = "뽀로로";
		String updatedType = "산업";
		// when & then
		return List.of(
			DynamicTest.dynamicTest("컨퍼런스명을 수정합니다.", () -> {
					//given
					ConferenceUpdateRequest request = new ConferenceUpdateRequest(
						updatedName, null, null, null, null, null, null, null, null
					);
					//when
					ConferenceUpdateResponse result = conferenceService.updateConference(identifier, conferenceId, request);
					//then
					assertThat(result)
						.extracting(ConferenceUpdateResponse::name,
							ConferenceUpdateResponse::organizer,
							ConferenceUpdateResponse::startDate,
							ConferenceUpdateResponse::startTime,
							ConferenceUpdateResponse::endDate,
							ConferenceUpdateResponse::endTime,
							ConferenceUpdateResponse::location,
							ConferenceUpdateResponse::position,
							ConferenceUpdateResponse::type)
						.containsExactly(
							updatedName,
							savedConference.getOrganizer(),
							savedConference.getPeriod().getStartDate(),
							savedConference.getPeriod().getStartTime(),
							savedConference.getPeriod().getEndDate(),
							savedConference.getPeriod().getEndTime(),
							savedConference.getLocation(),
							savedConference.getPosition(),
							savedConference.getType()
						);
				}
			),
			DynamicTest.dynamicTest("위치 정보를 수정합니다.", () -> {
					//given
					ConferenceUpdateRequest request = new ConferenceUpdateRequest(
						null, null, null, null, null,
						null, updatedLocation, null, null
					);
					//when
					ConferenceUpdateResponse result = conferenceService.updateConference(identifier, conferenceId, request);
					//then
					assertThat(result)
						.extracting(ConferenceUpdateResponse::name,
							ConferenceUpdateResponse::organizer,
							ConferenceUpdateResponse::startDate,
							ConferenceUpdateResponse::startTime,
							ConferenceUpdateResponse::endDate,
							ConferenceUpdateResponse::endTime,
							ConferenceUpdateResponse::location,
							ConferenceUpdateResponse::position,
							ConferenceUpdateResponse::type)
						.containsExactly(
							updatedName,
							savedConference.getOrganizer(),
							savedConference.getPeriod().getStartDate(),
							savedConference.getPeriod().getStartTime(),
							savedConference.getPeriod().getEndDate(),
							savedConference.getPeriod().getEndTime(),
							updatedLocation,
							savedConference.getPosition(),
							savedConference.getType()
						);
				}
			),
			DynamicTest.dynamicTest("컨퍼런스 기간을 수정합니다.", () -> {
					//given
					ConferenceUpdateRequest request = new ConferenceUpdateRequest(
						null, null, updatedStartDate, updatedStartTime, updatedEndDate, updatedEndTime,
						null, null, null
					);

					//when
					ConferenceUpdateResponse result = conferenceService.updateConference(identifier, conferenceId, request);

					//then
					assertThat(result)
						.extracting(ConferenceUpdateResponse::name,
							ConferenceUpdateResponse::organizer,
							ConferenceUpdateResponse::startDate,
							ConferenceUpdateResponse::startTime,
							ConferenceUpdateResponse::endDate,
							ConferenceUpdateResponse::endTime,
							ConferenceUpdateResponse::location,
							ConferenceUpdateResponse::position,
							ConferenceUpdateResponse::type)
						.containsExactly(
							updatedName,
							savedConference.getOrganizer(),
							updatedStartDate,
							updatedStartTime,
							updatedEndDate,
							updatedEndTime,
							updatedLocation,
							savedConference.getPosition(),
							savedConference.getType()
						);
				}
			),
			DynamicTest.dynamicTest("주최자를 수정합니다.", () -> {
					//given
					ConferenceUpdateRequest request = new ConferenceUpdateRequest(
						null, updatedOrganizer, null, null, null, null
						, null, null, null
					);
					//when
					ConferenceUpdateResponse result = conferenceService.updateConference(identifier, conferenceId, request);
					//then
					assertThat(result)
						.extracting(ConferenceUpdateResponse::name,
							ConferenceUpdateResponse::organizer,
							ConferenceUpdateResponse::startDate,
							ConferenceUpdateResponse::startTime,
							ConferenceUpdateResponse::endDate,
							ConferenceUpdateResponse::endTime,
							ConferenceUpdateResponse::location,
							ConferenceUpdateResponse::position,
							ConferenceUpdateResponse::type)
						.containsExactly(
							updatedName,
							updatedOrganizer,
							updatedStartDate,
							updatedStartTime,
							updatedEndDate,
							updatedEndTime,
							updatedLocation,
							savedConference.getPosition(),
							savedConference.getType()
						);
				}
			),
			DynamicTest.dynamicTest("유형을 수정합니다.", () -> {
					//given
					ConferenceUpdateRequest request = new ConferenceUpdateRequest(
						null, null, null, null, null, null,
						null, null, updatedType
					);
					//when
					ConferenceUpdateResponse result = conferenceService.updateConference(identifier, conferenceId, request);
					//then
					assertThat(result)
						.extracting(ConferenceUpdateResponse::name,
							ConferenceUpdateResponse::organizer,
							ConferenceUpdateResponse::startDate,
							ConferenceUpdateResponse::startTime,
							ConferenceUpdateResponse::endDate,
							ConferenceUpdateResponse::endTime,
							ConferenceUpdateResponse::location,
							ConferenceUpdateResponse::position,
							ConferenceUpdateResponse::type)
						.containsExactly(
							updatedName,
							updatedOrganizer,
							updatedStartDate,
							updatedStartTime,
							updatedEndDate,
							updatedEndTime,
							updatedLocation,
							savedConference.getPosition(),
							updatedType
						);
				}
			)
		);
	}

	@DisplayName("컨퍼런스별 참석자 정보 조회 시 관련 통계 정보를 반환한다.")
	@Test
	void findConferenceAttendeeInfo_returnsStatistics() {
		// given
		String identifier = "ADMIN123";
		Admin admin = Admin.of(identifier);
		Conference conference = Conference.of(
			"AI 컨퍼런스",
			TimePeriod.of(
				LocalDate.of(2025, 5, 1),
				LocalDate.of(2025, 5, 2),
				LocalTime.of(9, 0),
				LocalTime.of(18, 0)
			),
			"홍길동",
			"서울 코엑스",
			"1층",
			"IT"
		);
		admin.addConference(conference);
		adminRepository.save(admin);
		conferenceRepository.save(conference);

		Long conferenceId = conference.getId();

		// when
		ConferenceAttendeeInfoResDto result = conferenceService.findConferenceAttendeeInfo(identifier, conferenceId);

		// then
		assertThat(result).isNotNull();
		assertThat(result.serviceAttendeeCount()).isEqualTo(attendeeRepository.count());
		assertThat(result.boothAttendeeCount()).isZero();
		assertThat(result.conferenceAttendeeCount()).isZero();
		assertThat(result.sessionAttendeeCount()).isZero();
	}

}
