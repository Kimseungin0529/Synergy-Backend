package com.synergy.backend.domain.member.api;

import static com.synergy.backend.domain.member.entity.details.ExperienceLevelType.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;

import com.synergy.backend.domain.member.api.dto.request.AttendeeFilterRequest;
import com.synergy.backend.domain.member.api.dto.resposne.AttendeeListResponse;
import com.synergy.backend.domain.member.api.dto.resposne.AttendeeSimpleResponseDto;
import com.synergy.backend.domain.member.api.dto.resposne.RecruiterMyInfoResponseDto;
import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.domain.member.entity.User;
import com.synergy.backend.global.security.CustomUserDetails;
import com.synergy.backend.module.ControllerTestSupport;

class RecruiterControllerTest extends ControllerTestSupport {

	@DisplayName("채용담당자가 참가자 목록을 조회합니다.")
	@Test
	@WithMockUser(username = "RC12345", roles = {"RECRUITER"})
	void getAttendees() throws Exception {
		// given
		Long recruiterId = 1L;
		List<String> occupations = List.of("백엔드", "프론트엔드");
		String educationLevel = "4년제 졸업";
		String ageGroup = "20~24세 이하";
		String experienceLevel = "1~2년 이하";
		List<String> desiredRegions = List.of("수도권", "부산광역시");

		AttendeeFilterRequest requestCondition = AttendeeFilterRequest.of(
			occupations, educationLevel, ageGroup, experienceLevel, desiredRegions
		);

		// 전부다 수도권, 부산광역시 / 4년제 졸업, 20~24세 이하 라는 가정
		List<AttendeeSimpleResponseDto> attendeesCandidates = List.of(
			new AttendeeSimpleResponseDto(1L, "김지원1", "백엔드 개발자", JUNIOR, "Java, Spring", null, false),
			new AttendeeSimpleResponseDto(2L, "김지원2", "백엔드 개발자", JUNIOR, "Java, Spring", null, true),
			new AttendeeSimpleResponseDto(3L, "김지원3", "백엔드 개발자", MID_LEVEL, "Java, Spring", null, true),
			new AttendeeSimpleResponseDto(3L, "김지원4", "백엔드 개발자", MID_LEVEL, "Java, Spring", null, true),
			new AttendeeSimpleResponseDto(4L, "김지원5", "백엔드 개발자", JUNIOR, "Java, Spring", null, true),
			new AttendeeSimpleResponseDto(5L, "김지원6", "프론트엔드 개발자", JUNIOR, "React", null, true),
			new AttendeeSimpleResponseDto(6L, "김지원7", "프론트엔드 개발자", JUNIOR, "React", null, true),
			new AttendeeSimpleResponseDto(7L, "김지원8", "프론트엔드 개발자", JUNIOR, "React", null, true),
			new AttendeeSimpleResponseDto(8L, "김지원9", "프론트엔드 개발자", MID_LEVEL, "React", null, true),
			new AttendeeSimpleResponseDto(9L, "김지원10", "풀스택 개발자", JUNIOR, "All", null, true),
			new AttendeeSimpleResponseDto(10L, "김지원11", "풀스택 개발자", JUNIOR, "All", null, true),
			new AttendeeSimpleResponseDto(11L, "정서연12", "UI/UX 디자이너", MID_LEVEL, "Figma", null, true),
			new AttendeeSimpleResponseDto(12L, "정서연13", "UI/UX 디자이너", JUNIOR, "Figma", null, true)
		);

		List<AttendeeSimpleResponseDto> attendees = List.of(
			new AttendeeSimpleResponseDto(1L, "김지원1", "백엔드 개발자", JUNIOR, "Java, Spring", null, false),
			new AttendeeSimpleResponseDto(2L, "김지원2", "백엔드 개발자", JUNIOR, "Java, Spring", null, true),
			new AttendeeSimpleResponseDto(5L, "김지원5", "백엔드 개발자", JUNIOR, "Java, Spring", null, true),
			new AttendeeSimpleResponseDto(6L, "김지원6", "프론트엔드 개발자", JUNIOR, "React", null, true),
			new AttendeeSimpleResponseDto(7L, "김지원7", "프론트엔드 개발자", JUNIOR, "React", null, true),
			new AttendeeSimpleResponseDto(8L, "김지원8", "프론트엔드 개발자", JUNIOR, "React", null, true)
		);

		Page<AttendeeSimpleResponseDto> page = new PageImpl<>(attendees, PageRequest.of(0, 10), attendees.size());
		AttendeeListResponse response = AttendeeListResponse.from(page);

		given(recruiterService.getAttendeesBy(any(Pageable.class), eq(recruiterId), eq(requestCondition)))
			.willReturn(response);
		given(jwtProvider.validateAccessToken(anyString())).willReturn(true);
		//given(jwtProvider.getEmailOrAuthCodeFromToken(anyString())).willReturn("RC12345");
		given(jwtProvider.getRoleTypeFromToken(anyString())).willReturn(RoleType.RECRUITER);
		given(userDetailsService.loadUserByUsername(anyString())).willReturn(mock(UserDetails.class));

		// when & then
		mockMvc.perform(get("/api/v1/recruiter/{id}/attendees", recruiterId)
				.with(csrf())
				.param("occupations", occupations.get(0), occupations.get(1))
				.param("educationLevel", educationLevel)
				.param("ageGroup", ageGroup)
				.param("experienceLevel", experienceLevel)
				.param("regions", desiredRegions.get(0), desiredRegions.get(1))
				.param("page", "0")
				.param("size", "20")
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.currentPageNumber").value(0))
			.andExpect(jsonPath("$.data.totalElements").value(6))
			.andExpect(jsonPath("$.data.list[0].name").value("김지원1"))
			.andExpect(jsonPath("$.data.list[0].desiredJobPosition").value("백엔드 개발자"))
			.andExpect(jsonPath("$.data.list[0].experienceLevel").value(JUNIOR.getDescription()))
			.andExpect(jsonPath("$.data.list[1].name").value("김지원2"))
			.andExpect(jsonPath("$.data.list[1].desiredJobPosition").value("백엔드 개발자"))
			.andExpect(jsonPath("$.data.list[1].experienceLevel").value(JUNIOR.getDescription()))
			.andExpect(jsonPath("$.data.list[2].name").value("김지원5"))
			.andExpect(jsonPath("$.data.list[2].desiredJobPosition").value("백엔드 개발자"))
			.andExpect(jsonPath("$.data.list[2].experienceLevel").value(JUNIOR.getDescription()))
			.andExpect(jsonPath("$.data.list[3].name").value("김지원6"))
			.andExpect(jsonPath("$.data.list[3].desiredJobPosition").value("프론트엔드 개발자"))
			.andExpect(jsonPath("$.data.list[3].experienceLevel").value(JUNIOR.getDescription()))
			.andExpect(jsonPath("$.data.list[4].name").value("김지원7"))
			.andExpect(jsonPath("$.data.list[4].desiredJobPosition").value("프론트엔드 개발자"))
			.andExpect(jsonPath("$.data.list[4].experienceLevel").value(JUNIOR.getDescription()))
			.andExpect(jsonPath("$.data.list[5].name").value("김지원8"))
			.andExpect(jsonPath("$.data.list[5].desiredJobPosition").value("프론트엔드 개발자"))
			.andExpect(jsonPath("$.data.list[5].experienceLevel").value(JUNIOR.getDescription()))
			.andDo(print());
	}

	@Test
	@DisplayName("채용담당자가 자신의 정보를 조회합니다.")
	void getMyInformation() throws Exception {
		// given
		User user = mock(User.class);
		given(user.getId()).willReturn(1L);
		given(user.getIdentifier()).willReturn("RC12345");
		given(user.getRole()).willReturn(RoleType.RECRUITER);

		CustomUserDetails customUserDetails = new CustomUserDetails(user);

		UsernamePasswordAuthenticationToken authentication =
			new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		given(recruiterService.getMyInformation(1L)).willReturn(
			new RecruiterMyInfoResponseDto(
				"https://example.com/photo.png",
				"홍길동",
				"시너지",
				"프론트엔드 채용 담당자"
			)
		);

		// when & then
		mockMvc.perform(get("/api/v1/recruiter/my")
				.with(csrf())
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.companyPhotoUrl").value("https://example.com/photo.png"))
			.andExpect(jsonPath("$.data.recruiterName").value("홍길동"))
			.andExpect(jsonPath("$.data.company").value("시너지"))
			.andExpect(jsonPath("$.data.responsibility").value("프론트엔드 채용 담당자"))
			.andDo(print());
	}

	@DisplayName("채용담당자가 참가자에게 좋아요를 누릅니다.")
	@Test
	@WithMockUser(username = "RC12345", roles = {"RECRUITER"})
	void likeAttendee() throws Exception {
		// given
		Long attendeeId = 1L;
		given(jwtProvider.validateAccessToken(anyString())).willReturn(true);
		given(jwtProvider.getRoleTypeFromToken(anyString())).willReturn(RoleType.RECRUITER);

		User user = mock(User.class);
		given(user.getId()).willReturn(1L);
		given(user.getIdentifier()).willReturn("RC12345");
		given(user.getRole()).willReturn(RoleType.RECRUITER);

		CustomUserDetails customUserDetails = new CustomUserDetails(user);
		UsernamePasswordAuthenticationToken authentication =
			new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// when & then
		mockMvc.perform(post("/api/v1/recruiter/attendees/{attendeeId}/like", attendeeId)
				.with(csrf())
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@DisplayName("채용담당자가 참가자에게 좋아요를 취소합니다.")
	@Test
	@WithMockUser(username = "RC12345", roles = {"RECRUITER"})
	void unlikeAttendee() throws Exception {
		// given
		Long attendeeId = 1L;
		given(jwtProvider.validateAccessToken(anyString())).willReturn(true);
		given(jwtProvider.getRoleTypeFromToken(anyString())).willReturn(RoleType.RECRUITER);

		User user = mock(User.class);
		given(user.getId()).willReturn(1L);
		given(user.getIdentifier()).willReturn("RC12345");
		given(user.getRole()).willReturn(RoleType.RECRUITER);

		CustomUserDetails customUserDetails = new CustomUserDetails(user);
		UsernamePasswordAuthenticationToken authentication =
			new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// when & then
		mockMvc.perform(delete("/api/v1/recruiter/attendees/{attendeeId}/unlike", attendeeId)
				.with(csrf())
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@DisplayName("채용담당자가 좋아요한 참가자 목록을 조회합니다.")
	@Test
	@WithMockUser(username = "RC12345", roles = {"RECRUITER"})
	void getLikedAttendees() throws Exception {
		// given
		List<AttendeeSimpleResponseDto> likedAttendees = List.of(
			new AttendeeSimpleResponseDto(1L, "참가자1", "백엔드 개발자", JUNIOR, "Java", null, true),
			new AttendeeSimpleResponseDto(2L, "참가자2", "프론트엔드 개발자", JUNIOR, "React", null, true)
		);
		given(jwtProvider.validateAccessToken(anyString())).willReturn(true);
		given(jwtProvider.getRoleTypeFromToken(anyString())).willReturn(RoleType.RECRUITER);

		User user = mock(User.class);
		given(user.getId()).willReturn(1L);
		given(user.getIdentifier()).willReturn("RC12345");
		given(user.getRole()).willReturn(RoleType.RECRUITER);

		CustomUserDetails customUserDetails = new CustomUserDetails(user);
		UsernamePasswordAuthenticationToken authentication =
			new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		given(recruiterAttendeeLikeService.getLikedAttendees(anyLong())).willReturn(likedAttendees);

		// when & then
		mockMvc.perform(get("/api/v1/recruiter/me/liked-attendees")
				.with(csrf())
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data[0].name").value("참가자1"))
			.andExpect(jsonPath("$.data[1].name").value("참가자2"))
			.andDo(print());
	}
}
