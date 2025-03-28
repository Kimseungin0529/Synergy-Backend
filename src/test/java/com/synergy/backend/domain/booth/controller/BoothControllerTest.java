package com.synergy.backend.domain.booth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.synergy.backend.domain.booth.dto.BoothDetailResponseDto;
import com.synergy.backend.domain.booth.dto.BoothRequestDto;
import com.synergy.backend.domain.booth.service.BoothService;
import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.global.jwt.JwtProvider;
import com.synergy.backend.global.security.CustomUserDetailsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = BoothController.class)
@ActiveProfiles("test")
class BoothControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    BoothService boothService;
    @MockitoBean
    JwtProvider jwtProvider;
    @MockitoBean
    CustomUserDetailsService userDetailsService;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @DisplayName("부스를 생성한다.")
    @Test
    @WithMockUser(username = "AUTH1", roles = {"ADMIN"})
    void createBooth() throws Exception {
        // given
        Long conferenceId = 1L;
        String router = "https://your-domain.com/";
        BoothRequestDto requestDto = new BoothRequestDto(
                "CodeSphere",
                "스타트업",
                LocalDate.of(3025, 10, 11),
                "C Hall",
                "101C",
                "CodeSphere는 글로벌 클라우드 서비스 기업입니다."
        );

        BoothDetailResponseDto responseDto = new BoothDetailResponseDto(
                10L,
                requestDto.companyName(),
                requestDto.companyType(),
                requestDto.boothLocation(),
                requestDto.boothNumber(),
                requestDto.progressDate(),
                requestDto.boothDescription(),
                "https://qr-url.com/booth123",
                "https://image-url.com/booth123"
        );

        MockMultipartFile imageFile = new MockMultipartFile(
                "imageFile", "booth.jpg", "image/jpeg", "fake-image-content" .getBytes()
        );

        MockMultipartFile jsonPart = new MockMultipartFile(
                "requestDto", null, "application/json",
                objectMapper.writeValueAsBytes(requestDto)
        );

        given(boothService.createBooth(eq(conferenceId), eq(router), any(BoothRequestDto.class), any(MultipartFile.class)))
                .willReturn(responseDto);

        given(jwtProvider.validateToken(anyString())).willReturn(true);
        given(jwtProvider.getIdentifierFromToken(anyString())).willReturn("AUTH1");
        given(jwtProvider.getRoleTypeFromToken(anyString())).willReturn(RoleType.ADMIN);
        given(userDetailsService.loadUserByUsername(anyString())).willReturn(mock(UserDetails.class));

        // when & then
        mockMvc.perform(multipart("/api/v1/conference/{conferenceId}/booths", conferenceId)
                        .file(imageFile)
                        .file(jsonPart)
                        .with(csrf())
                        .header("Origin", router)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(10L))
                .andExpect(jsonPath("$.data.companyName").value("CodeSphere"));
    }


}