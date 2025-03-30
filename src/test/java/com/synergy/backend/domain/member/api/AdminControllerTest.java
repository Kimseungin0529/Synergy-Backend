package com.synergy.backend.domain.member.api;

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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;

import com.synergy.backend.domain.member.api.dto.resposne.AttendeeLevelRankingResponseDto;
import com.synergy.backend.domain.member.api.dto.resposne.AttendeePointRankingResponseDto;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.domain.member.entity.details.MembershipLevelType;
import com.synergy.backend.module.ControllerTestSupport;

class AdminControllerTest extends ControllerTestSupport {

	@DisplayName("관리자가 등급별 참가자 랭킹을 조회합니다.")
	@Test
	@WithMockUser(username = "AD12345", roles = {"ADMIN"})
	void getAttendeeLevelRankings() throws Exception {
		// given
		Attendee attendee1 = Attendee.of("email1@example.com", "encodedPassword", "참가자1", "01000000000");
		attendee1.addTotalPoints(800);
		Attendee attendee2 = Attendee.of("email2@example.com", "encodedPassword", "참가자2", "01011111111");
		attendee2.addTotalPoints(10);

		List<AttendeeLevelRankingResponseDto> rankingList = List.of(
			AttendeeLevelRankingResponseDto.from(attendee1)
		);
		Page<AttendeeLevelRankingResponseDto> page = new PageImpl<>(rankingList, PageRequest.of(0, 10),
			rankingList.size());

		given(adminService.getAttendeeLevelRankings(any(), any())).willReturn(page);
		given(jwtProvider.validateAccessToken(anyString())).willReturn(true);
		given(jwtProvider.getRoleTypeFromToken(anyString())).willReturn(RoleType.ADMIN);
		given(userDetailsService.loadUserByUsername(anyString())).willReturn(mock(UserDetails.class));

		// when & then
		mockMvc.perform(get("/api/v1/admin/attendees/level-rankings")
				.param("membershipLevelType", MembershipLevelType.GOLD.name())
				.param("page", "0")
				.param("size", "10")
				.with(csrf())
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.totalElements").value(1))
			.andExpect(jsonPath("$.data.content[0].attendeeName").value("참가자1"))
			.andExpect(jsonPath("$.data.content[0].membershipLevel").value(MembershipLevelType.GOLD.name()))
			.andDo(print());
	}

	@DisplayName("관리자가 포인트 랭킹을 조회합니다.")
	@Test
	@WithMockUser(username = "AD12345", roles = {"ADMIN"})
	void getAttendeePointRankings() throws Exception {
		// given
		Attendee attendee1 = Attendee.of("email1@example.com", "encodedPassword", "참가자1", "010-0000-0000");
		attendee1.addTotalPoints(100);
		Attendee attendee2 = Attendee.of("email2@example.com", "encodedPassword", "참가자2", "010-1111-1111");
		attendee2.addTotalPoints(80);

		List<AttendeePointRankingResponseDto> rankingList = List.of(
			AttendeePointRankingResponseDto.from(attendee1),
			AttendeePointRankingResponseDto.from(attendee2)
		);
		Page<AttendeePointRankingResponseDto> page = new PageImpl<>(rankingList, PageRequest.of(0, 10),
			rankingList.size());

		given(adminService.getAttendeePointRankings(any())).willReturn(page);
		given(jwtProvider.validateAccessToken(anyString())).willReturn(true);
		given(jwtProvider.getRoleTypeFromToken(anyString())).willReturn(RoleType.ADMIN);
		given(userDetailsService.loadUserByUsername(anyString())).willReturn(mock(UserDetails.class));

		// when & then
		mockMvc.perform(get("/api/v1/admin/attendees/point-rankings")
				.param("page", "0")
				.param("size", "10")
				.with(csrf())
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.totalElements").value(2))
			.andExpect(jsonPath("$.data.content[0].attendeeName").value("참가자1"))
			.andExpect(jsonPath("$.data.content[0].totalPoints").value(100))
			.andDo(print());
	}
}
