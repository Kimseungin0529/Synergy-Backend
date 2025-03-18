package com.synergy.backend.domain.conference.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.synergy.backend.domain.conference.dto.requset.ConferenceCreateRequest;
import com.synergy.backend.domain.conference.dto.response.ConferenceCreateResponse;
import com.synergy.backend.domain.conference.service.ConferenceService;
import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.global.security.CustomUserDetailsService;
import com.synergy.backend.global.security.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ConferenceController.class)
class ConferenceControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    ConferenceService conferenceService;

    @MockitoBean
    JwtProvider jwtProvider;
    @MockitoBean
    CustomUserDetailsService userDetailsService;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    /**
     * [@MockitoBean 를 서비스 계층 이외에도 사용한 이유]
     * JwtAuthenticationFilter 필터가 스프링 빈으로 등록되었기에 MockBean 처리가 필수입니다.
     * 필터를 추가하는 방식에 스프링 빈으로 등록하면 컨트롤러 테스트 과정에서 따로 추가해줘야 할 거 같습니다.
     * 스프링 시큐리티 빈에서 따로 주입해주는 방법도 있으니 추후, 확정해도 괜찮아 보입니다.
     */

    @DisplayName("컨퍼런스를 등록한다.")
    @Test
    @WithMockUser(username = "AUTH1", roles = {"ADMIN"})
    void registerConference() throws Exception {
        // given
        ConferenceCreateRequest request = new ConferenceCreateRequest(
                "Spring Boot Conference 2025",
                LocalDateTime.of(2025, 6, 15, 10, 0),
                LocalDateTime.of(2025, 6, 16, 18, 0),
                "Seoul, South Korea",
                "김승진",
                "IT"
        );
        ConferenceCreateResponse response = new ConferenceCreateResponse(1L);

        given(conferenceService.registerConference(request))
                .willReturn(response);
        given(jwtProvider.validateToken(anyString()))
                .willReturn(true);
        given(jwtProvider.getEmailOrAuthCodeFromToken(anyString()))
                .willReturn("AUTH1");
        given(jwtProvider.getRoleTypeFromToken(anyString()))
                .willReturn(RoleType.ADMIN);
        given(userDetailsService.loadUserByUsername(anyString()))
                .willReturn(mock(UserDetails.class));


        // when & then
        mockMvc.perform(post("/api/v1/conference")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(1L));


    }


}