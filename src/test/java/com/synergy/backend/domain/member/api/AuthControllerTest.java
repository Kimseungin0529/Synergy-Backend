package com.synergy.backend.domain.member.api;

import com.synergy.backend.domain.member.api.dto.request.LoginAdminRequestDto;
import com.synergy.backend.domain.member.api.dto.request.LoginAttendeeRequestDto;
import com.synergy.backend.domain.member.api.dto.request.SignupAttendeeRequestDto;
import com.synergy.backend.domain.member.api.dto.response.TokenResponseDto;
import com.synergy.backend.domain.member.vo.TokenWithRefreshToken;
import com.synergy.backend.global.annotation.DisableSwaggerSecurity;
import com.synergy.backend.global.common.ApiResponse;
import com.synergy.backend.module.ControllerTestSupport;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.synergy.backend.domain.member.entity.RoleType.ATTENDEE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends ControllerTestSupport {


    /**
     * 응답 헤더(쿠키 등)에 대한 검증은 다른 요소에서 필요해 보임.
     */
    @DisplayName("참가자 권한으로 회원가입합니다.")
    @Test
    @WithMockUser(username = "hong@example.com", roles = {"ATTENDEE"})
    void registerAttendee() throws Exception {
        // given
        TokenResponseDto tokenResponseDto = new TokenResponseDto("aaaAccess.bbb.ccc", "hong@example.com", ATTENDEE, 1L);
        TokenWithRefreshToken response = TokenWithRefreshToken.of("aaaRefresh.bbb.ccc", tokenResponseDto);

        String name = "홍길동";
        String ticketCode = "ab12habK";
        String email = "hong@example.com";
        String password = "test1234!@#";
        String phone = "01088794304";
        SignupAttendeeRequestDto requestBody = new SignupAttendeeRequestDto(name, email, ticketCode, password, phone);


        given(authService.registerAttendee(requestBody))
                .willReturn(response);
        willDoNothing().given(cookieUtils)
                .addRefreshTokenToCookie(any(HttpServletResponse.class), anyString());

        // when & then
        mockMvc.perform(post("/api/v1/auth/attendee/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").value("aaaAccess.bbb.ccc"))
                .andExpect(jsonPath("$.data.identifier").value(email))
                .andExpect(jsonPath("$.data.role").value("ATTENDEE"))
                .andExpect(jsonPath("$.data.id").isNumber());

        verify(cookieUtils)
                .addRefreshTokenToCookie(any(HttpServletResponse.class), anyString());
    }

    @DisplayName("참가자 권한으로 로그인합니다.")
    @Test
    @WithMockUser(username = "hong@example.com", roles = {"ATTENDEE"})
    void loginAttendee() throws Exception {
        // given
        TokenResponseDto tokenResponseDto = new TokenResponseDto("aaaAccess.bbb.ccc", "hong@example.com", ATTENDEE, 1L);
        TokenWithRefreshToken response = TokenWithRefreshToken.of("aaaRefresh.bbb.ccc", tokenResponseDto);

        String email = "hong@example.com";
        String password = "test1234!@#";
        LoginAttendeeRequestDto requestBody = new LoginAttendeeRequestDto(email, password);


        given(authService.loginAsAttendee(requestBody.email(), requestBody.password()))
                .willReturn(response);

        willDoNothing().given(cookieUtils)
                .addRefreshTokenToCookie(any(HttpServletResponse.class), anyString());

        // when & then
        mockMvc.perform(post("/api/v1/auth/attendee/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").value("aaaAccess.bbb.ccc"))
                .andExpect(jsonPath("$.data.identifier").value(email))
                .andExpect(jsonPath("$.data.role").value("ATTENDEE"))
                .andExpect(jsonPath("$.data.id").isNumber());

        verify(cookieUtils)
                .addRefreshTokenToCookie(any(HttpServletResponse.class), anyString());
    }


    @DisableSwaggerSecurity
    @PostMapping("/admin/login")
    public ApiResponse<TokenResponseDto> loginAdmin(@RequestBody LoginAdminRequestDto request,
                                                    HttpServletResponse response) {

        TokenWithRefreshToken tokenWithRefreshToken = authService.loginAsAdminOrRecruiter(request.adminAuthCode());

        cookieUtils.addRefreshTokenToCookie(response, tokenWithRefreshToken.refreshToken());

        return ApiResponse.ok(tokenWithRefreshToken.tokenResponseDto(), 200);
    }


    @DisplayName("관리자/채용담당자 권한으로 로그인합니다.")
    @Test
    @WithMockUser(username = "ADM_test_1", roles = {"ATTENDEE"})
    void loginAdmin() throws Exception {
        // given
        TokenResponseDto tokenResponseDto = new TokenResponseDto("aaaAccess.bbb.ccc", "ADM_test_1", ATTENDEE, 1L);
        TokenWithRefreshToken response = TokenWithRefreshToken.of("aaaRefresh.bbb.ccc", tokenResponseDto);

        String authCode = "ADM_test_1";
        LoginAdminRequestDto requestBody = new LoginAdminRequestDto(authCode);


        given(authService.loginAsAdminOrRecruiter(requestBody.adminAuthCode()))
                .willReturn(response);

        willDoNothing().given(cookieUtils)
                .addRefreshTokenToCookie(any(HttpServletResponse.class), anyString());

        // when & then
        mockMvc.perform(post("/api/v1/auth/admin/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").value("aaaAccess.bbb.ccc"))
                .andExpect(jsonPath("$.data.identifier").value("ADM_test_1"))
                .andExpect(jsonPath("$.data.role").value("ATTENDEE"))
                .andExpect(jsonPath("$.data.id").isNumber());

        verify(cookieUtils)
                .addRefreshTokenToCookie(any(HttpServletResponse.class), anyString());
    }


}