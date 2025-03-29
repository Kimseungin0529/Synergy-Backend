package com.synergy.backend.module;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.synergy.backend.domain.booth.controller.BoothController;
import com.synergy.backend.domain.booth.service.BoothService;
import com.synergy.backend.domain.conference.api.ConferenceController;
import com.synergy.backend.domain.conference.service.ConferenceService;
import com.synergy.backend.domain.member.api.RecruiterController;
import com.synergy.backend.domain.member.service.RecruiterAttendeeLikeService;
import com.synergy.backend.domain.member.service.RecruiterService;
import com.synergy.backend.global.jwt.JwtProvider;
import com.synergy.backend.global.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {BoothController.class, ConferenceController.class, RecruiterController.class} /* 추가로 필요한 컨트롤러 클래스 지정 */)
public abstract class ControllerTestSupport {
    @Autowired
    protected MockMvc mockMvc;

    @MockitoBean
    protected BoothService boothService;
    @MockitoBean
    protected ConferenceService conferenceService;
    @MockitoBean
    protected RecruiterService recruiterService;

    @MockitoBean
    protected RecruiterAttendeeLikeService recruiterAttendeeLikeService;


    @MockitoBean
    protected JwtProvider jwtProvider;
    @MockitoBean
    protected CustomUserDetailsService userDetailsService;


    protected final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
}