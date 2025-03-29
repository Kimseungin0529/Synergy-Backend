package com.synergy.backend.domain.booth.controller;

import com.synergy.backend.domain.booth.dto.BoothDetailResponseDto;
import com.synergy.backend.domain.booth.dto.BoothRequestDto;
import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.module.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class BoothControllerTest extends ControllerTestSupport {

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
                "https://image-url.com/booth123",
                Boolean.FALSE
        );

        MockMultipartFile imageFile = new MockMultipartFile(
                "imageFile", "booth.jpg", "image/jpeg", "fake-image-content" .getBytes()
        );

        MockMultipartFile jsonPart = new MockMultipartFile(
                "requestDto", null, "application/json",
                objectMapper.writeValueAsBytes(requestDto)
        );

        boothService.createBooth(eq(conferenceId), eq(router), any(BoothRequestDto.class), any(MultipartFile.class));

        given(jwtProvider.validateAccessToken(anyString())).willReturn(true);
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
                .andExpect(status().isOk());
    }


}