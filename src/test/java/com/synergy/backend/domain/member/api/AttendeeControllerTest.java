package com.synergy.backend.domain.member.api;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import com.synergy.backend.domain.member.api.dto.response.LikedRecruiterResponseDto;
import com.synergy.backend.domain.member.api.dto.response.ProfileImageUpdatedResponseDto;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.global.security.CustomUserDetails;
import com.synergy.backend.module.ControllerTestSupport;

@DisplayName("AttendeeController 테스트")
class AttendeeControllerTest extends ControllerTestSupport {

	@DisplayName("좋아요한 채용담당자 목록을 조회한다")
	@Test
	void getLikedRecruiters() throws Exception {
		// given
		Long attendeeId = 1L;
		CustomUserDetails userDetails = new CustomUserDetails(createAttendee(attendeeId));
		SecurityContextHolder.getContext().setAuthentication(
			new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
		);

		List<LikedRecruiterResponseDto> liked = List.of(
			new LikedRecruiterResponseDto("회사1", "책임1", "홍길동"),
			new LikedRecruiterResponseDto("회사2", "책임2", "김철수")
		);

		given(recruiterAttendeeLikeService.getLikedRecruiters(attendeeId)).willReturn(liked);

		// when & then
		mockMvc.perform(get("/api/v1/attendee/liked-recruiters")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.length()").value(2))
			.andExpect(jsonPath("$.data[0].company").value("회사1"))
			.andExpect(jsonPath("$.data[0].responsibility").value("책임1"))
			.andExpect(jsonPath("$.data[0].name").value("홍길동"))
			.andDo(print());
	}

	@Test
	@DisplayName("참가자 현재 직무 정보 등록 요청을 처리한다")
	void addJobInfo() throws Exception {
		Long attendeeId = 1L;
		CustomUserDetails userDetails = new CustomUserDetails(createAttendee(attendeeId));
		SecurityContextHolder.getContext().setAuthentication(
			new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
		);

		String requestBody = """
			{
			    "interestCodes": [101, 102, 103],
			    "jobGroupCode": 1,
			    "jobPositionCode": 101,
			    "hiringInterested": true
			}
			""";

		mockMvc.perform(patch("/api/v1/attendee/onboarding/job-info")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value(200))
			.andDo(print());

		then(attendeeService).should().addJobInfo(eq(attendeeId), any());
	}

	@DisplayName("내 정보를 조회한다")
	@Test
	void getMyInformation() throws Exception {
		Long attendeeId = 1L;
		CustomUserDetails userDetails = new CustomUserDetails(createAttendee(attendeeId));
		SecurityContextHolder.getContext().setAuthentication(
			new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
		);

		given(attendeeService.getMyInformation(attendeeId)).willReturn(null);

		mockMvc.perform(get("/api/v1/attendee/my")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value(200))
			.andDo(print());

		then(attendeeService).should().getMyInformation(attendeeId);
	}

	@DisplayName("특정 참가자의 상세 정보를 조회한다")
	@Test
	void getAttendeeInfoDetail() throws Exception {
		Long attendeeId = 1L;
		Long targetId = 2L;
		CustomUserDetails userDetails = new CustomUserDetails(createAttendee(attendeeId));
		SecurityContextHolder.getContext().setAuthentication(
			new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
		);

		given(attendeeService.getAttendeeInfoDetail(targetId, attendeeId, userDetails.getRole())).willReturn(null);

		mockMvc.perform(get("/api/v1/attendee/{attendeeId}", targetId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value(200))
			.andDo(print());

		then(attendeeService).should().getAttendeeInfoDetail(targetId, attendeeId, userDetails.getRole());
	}

	@DisplayName("참가자 상세 직무 정보 등록 요청을 처리한다")
	@Test
	void addJobInfoDetails() throws Exception {
		Long attendeeId = 1L;
		CustomUserDetails userDetails = new CustomUserDetails(createAttendee(attendeeId));
		SecurityContextHolder.getContext().setAuthentication(
			new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
		);

		MockMultipartFile request = new MockMultipartFile(
			"request",
			"request.json",
			MediaType.APPLICATION_JSON_VALUE,
			"""
				{
					"desiredJobGroupCode": 1,
					"desiredJobPositionCode": 101,
					"educationLevelCode": 3,
					"ageGroupCode": 20,
					"techStacks": "Java,Spring",
					"experienceLevelCode": 2,
					"desiredWorkRegionCodes": [11, 22],
					"selfIntroduction": "저는 열정적인 백엔드 개발자입니다.",
					"additionalInfo": "스타트업 인턴 경험, 외부 해커톤 참가",
					"workplaceSelectionFactorCodes": [1, 2],
					"preferredCorporateCultureCodes": [1],
					"conferencePurposeCodes": [1, 2]
				}
				""".getBytes()
		);

		MockMultipartFile profileImage = new MockMultipartFile("profileImage", "profile.png", MediaType.IMAGE_PNG_VALUE,
			"image content".getBytes());

		mockMvc.perform(multipart("/api/v1/attendee/onboarding/job-info-details")
				.file(request)
				.file(profileImage)
				.with(csrf())
				.with(requestBuilder -> {
					requestBuilder.setMethod("PATCH");
					return requestBuilder;
				})
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value(200))
			.andDo(print());

		then(attendeeService).should().addJobInfoDetails(eq(attendeeId), any(), any());
	}

	@DisplayName("참가자 프로필 사진을 수정한다")
	@Test
	void updateProfileImage() throws Exception {
		Long attendeeId = 1L;
		CustomUserDetails userDetails = new CustomUserDetails(createAttendee(attendeeId));
		SecurityContextHolder.getContext().setAuthentication(
			new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
		);

		MockMultipartFile profileImage = new MockMultipartFile("profileImage", "profile.png", MediaType.IMAGE_PNG_VALUE,
			"image content".getBytes());

		given(attendeeService.updateProfileImage(eq(attendeeId), any())).willReturn(
			ProfileImageUpdatedResponseDto.from("updated-url"));

		mockMvc.perform(multipart("/api/v1/attendee/profile-image")
				.file(profileImage)
				.with(csrf())
				.with(request -> {
					request.setMethod("PATCH");
					return request;
				})
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value(200))
			.andExpect(jsonPath("$.data.profileImageUrl").value("updated-url"))
			.andDo(print());

		then(attendeeService).should().updateProfileImage(eq(attendeeId), any());
	}

	private Attendee createAttendee(Long id) {
		Attendee attendee = Attendee.of("exaple@example.com", "pass", "참가자", "01101010");
		ReflectionTestUtils.setField(attendee, "id", id);
		return attendee;
	}
}
