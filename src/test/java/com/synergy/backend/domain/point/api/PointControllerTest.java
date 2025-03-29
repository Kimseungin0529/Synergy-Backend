package com.synergy.backend.domain.point.api;

import static com.synergy.backend.domain.point.entity.PointType.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.point.api.dto.PointResponseDto;
import com.synergy.backend.domain.point.entity.Point;
import com.synergy.backend.domain.point.entity.PointType;
import com.synergy.backend.global.security.CustomUserDetails;
import com.synergy.backend.module.ControllerTestSupport;

class PointControllerTest extends ControllerTestSupport {

	@DisplayName("참가자의 포인트 내역을 조회한다.")
	@Test
	@WithMockUser(roles = {"ATTENDEE"})
	void getMyPoints() throws Exception {
		// given
		Long attendeeId = 1L;
		CustomUserDetails userDetails = new CustomUserDetails(createAttendee(attendeeId));
		SecurityContextHolder.getContext().setAuthentication(
			new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
		);

		List<PointResponseDto> mockResponse = List.of(
			PointResponseDto.from(Point.of(SIGN_UP), "회원가입"),
			PointResponseDto.from(Point.of(PointType.SESSION_ATTEND), "세션참여")
		);
		given(pointService.getPointResponses(Mockito.anyLong()))
			.willReturn(mockResponse);

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/points/my-points")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value("success"))
			.andExpect(jsonPath("$.code").value(200))
			.andExpect(jsonPath("$.data").isArray())
			.andExpect(jsonPath("$.data").isArray())
			.andExpect(jsonPath("$.data[0].title").value(SIGN_UP.getMessage()))
			.andExpect(jsonPath("$.data[0].point").value(SIGN_UP.getPointValue()))
			.andExpect(jsonPath("$.data[0].details").value("회원가입"))
			.andExpect(jsonPath("$.data[1].title").value(SESSION_ATTEND.getMessage()))
			.andExpect(jsonPath("$.data[1].point").value(SESSION_ATTEND.getPointValue()))
			.andExpect(jsonPath("$.data[1].details").value("세션참여"))
			.andDo(print());

	}

	private Attendee createAttendee(Long id) {
		Attendee attendee = Attendee.of("exaple@example.com", "pass", "참가자", "01101010");
		ReflectionTestUtils.setField(attendee, "id", id);
		return attendee;
	}
}
