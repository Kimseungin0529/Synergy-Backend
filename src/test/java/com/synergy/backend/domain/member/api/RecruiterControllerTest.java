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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.synergy.backend.domain.member.api.dto.request.AttendeeFilterRequest;
import com.synergy.backend.domain.member.api.dto.resposne.AttendeeListResponse;
import com.synergy.backend.domain.member.api.dto.resposne.AttendeeSimpleResponseDto;
import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.domain.member.service.RecruiterAttendeeLikeService;
import com.synergy.backend.domain.member.service.RecruiterService;
import com.synergy.backend.global.jwt.JwtProvider;
import com.synergy.backend.global.security.CustomUserDetailsService;

@WebMvcTest(controllers = RecruiterController.class)
class RecruiterControllerTest {

	private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
	@Autowired
	MockMvc mockMvc;
	@MockitoBean
	RecruiterService recruiterService;
	@MockitoBean
	JwtProvider jwtProvider;
	@MockitoBean
	CustomUserDetailsService userDetailsService;
	@MockitoBean
	RecruiterAttendeeLikeService recruiterAttendeeLikeService;

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
		given(jwtProvider.validateToken(anyString())).willReturn(true);
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

}
