package com.synergy.backend.domain.conference.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.synergy.backend.domain.conference.dto.requset.ConferenceCreateRequest;
import com.synergy.backend.domain.conference.dto.requset.ConferenceUpdateRequest;
import com.synergy.backend.domain.conference.dto.response.ConferenceCreateResponse;
import com.synergy.backend.domain.conference.dto.response.ConferenceUpdateResponse;
import com.synergy.backend.domain.conference.service.ConferenceService;
import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.global.security.CustomUserDetailsService;
import com.synergy.backend.global.jwt.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
     *
     * [@WithMockUser 를 사용한 이유]
     * 실제로는 JWT 와 Authorization 헤더를 통해 검증하는데 이를 검증하기 위해서는 추가적인 의존성이 필요합니다.
     * 편의를 위해 @WithMockUser 사용하여 빠르게 해결했으며 해당 방법을 유지한다면 따로 JWT 와 Authorization 헤더
     * 에 대한 검증하는 테스트가 있으면 좋아 보입니다.
     */


    /**
     * 아래 테스트 코드는 올바르지 않습니다. 시간의 변화에 따라 테스트의 성공 유무가 결정됩니다.
     * 하지만 Validation 으로 Controller Request Dto 단에서 검증하기에 핸들링하기 어렵다고 판단했습니다.
     * 기한이 촉박하기에 아래와 같이 3000년까지 임시로 정했습니다. 추후 개선할 필요가 있습니다.
     */

    @DisplayName("컨퍼런스를 등록한다.")
    @Test
    @WithMockUser(username = "AUTH1", roles = {"ADMIN"})
    void registerConference() throws Exception {
        // given
        ConferenceCreateRequest request = new ConferenceCreateRequest(
                "Spring Boot Conference 2025",
                "김승진",
                LocalDate.of(3025, 6, 15),
                LocalTime.of(10, 0),
                LocalDate.of(3025, 6, 15),
                LocalTime.of( 10, 0),
                "Seoul, South Korea",
                "A로비",
                "IT"
        );
        ConferenceCreateResponse response = new ConferenceCreateResponse(1L);

        String identifier = "AUTH1";
        given(conferenceService.registerConference(eq(identifier), any(ConferenceCreateRequest.class))).willReturn(response);
        given(jwtProvider.validateToken(anyString())).willReturn(true);
        given(jwtProvider.getIdentifierFromToken(anyString())).willReturn("AUTH1");
        given(jwtProvider.getRoleTypeFromToken(anyString())).willReturn(RoleType.ADMIN);
        given(userDetailsService.loadUserByUsername(anyString())).willReturn(mock(UserDetails.class));


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


    @DisplayName("컨퍼런스 등록을 위해서 컨퍼런스명은 필수입나다.")
    @Test
    @WithMockUser(username = "AUTH1", roles = {"ADMIN"})
    void registerConference_ExceptionOfName() throws Exception {
        // given
        ConferenceCreateRequest request = new ConferenceCreateRequest(
                null,
                "김승진",
                LocalDate.of(3025, 6, 15),
                LocalTime.of(10, 0),
                LocalDate.of(3025, 6, 15),
                LocalTime.of( 18, 0),
                "Seoul, South Korea",
                "A로비",
                "IT"


        );

        given(jwtProvider.validateToken(anyString())).willReturn(true);
        given(jwtProvider.getIdentifierFromToken(anyString())).willReturn("AUTH1");
        given(jwtProvider.getRoleTypeFromToken(anyString())).willReturn(RoleType.ADMIN);
        given(userDetailsService.loadUserByUsername(anyString())).willReturn(mock(UserDetails.class));

        // when & then
        mockMvc.perform(post("/api/v1/conference")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("컨퍼런스명은 필수입니다. 공백 이하는 불가능합니다."));


    }


    @DisplayName("컨퍼런스 등록 시 시작 날짜는 반드시 미래여야 한다.")
    @Test
    @WithMockUser(username = "AUTH1", roles = {"ADMIN"})
    void registerConference_ExceptionOfStartDate() throws Exception {
        // given
        ConferenceCreateRequest request = new ConferenceCreateRequest(
                "카카오 대규모 IT 행사",
                "김승진",
                LocalDate.of(2024, 6, 15),
                LocalTime.of(13, 0),
                LocalDate.of(3025, 6, 18),
                LocalTime.of( 18, 0),
                "Seoul, South Korea",
                "A로비",
                "IT"
        );

        given(jwtProvider.validateToken(anyString())).willReturn(true);
        given(jwtProvider.getIdentifierFromToken(anyString())).willReturn("AUTH1");
        given(jwtProvider.getRoleTypeFromToken(anyString())).willReturn(RoleType.ADMIN);
        given(userDetailsService.loadUserByUsername(anyString())).willReturn(mock(UserDetails.class));

        // when & then
        mockMvc.perform(post("/api/v1/conference")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("시작 날짜는 미래여야 합니다."));


    }

    @DisplayName("컨퍼런스 등록 시 종료 날짜는 반드시 미래여야 한다.")
    @Test
    @WithMockUser(username = "AUTH1", roles = {"ADMIN"})
    void registerConference_ExceptionOfEndDate() throws Exception {
        // given
        ConferenceCreateRequest request = new ConferenceCreateRequest(
                "카카오 대규모 IT 행사",
                "김승진",
                LocalDate.of(3024, 6, 15),
                LocalTime.of(13, 0),
                LocalDate.of(2025, 3, 18),
                LocalTime.of( 18, 0),
                "Seoul, South Korea",
                "로비 BB",
                "IT"
        );

        given(jwtProvider.validateToken(anyString())).willReturn(true);
        given(jwtProvider.getIdentifierFromToken(anyString())).willReturn("AUTH1");
        given(jwtProvider.getRoleTypeFromToken(anyString())).willReturn(RoleType.ADMIN);
        given(userDetailsService.loadUserByUsername(anyString())).willReturn(mock(UserDetails.class));

        // when & then
        mockMvc.perform(post("/api/v1/conference")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("종료 날짜는 미래여야 합니다."));


    }


    @DisplayName("컨퍼런스 등록 시 위치 정보는 필수입니다.")
    @Test
    @WithMockUser(username = "AUTH1", roles = {"ADMIN"})
    void registerConference_ExceptionOfLocation() throws Exception {
        // given
        ConferenceCreateRequest request = new ConferenceCreateRequest(
                "카카오 대규모 IT 행사",
                "김승진",
                LocalDate.of(3024, 6, 15),
                LocalTime.of(13, 0),
                LocalDate.of(3022, 6, 18),
                LocalTime.of( 18, 0),
                "  ",
                "로비 A",
                "IT"
        );

        given(jwtProvider.validateToken(anyString())).willReturn(true);
        given(jwtProvider.getIdentifierFromToken(anyString())).willReturn("AUTH1");
        given(jwtProvider.getRoleTypeFromToken(anyString())).willReturn(RoleType.ADMIN);
        given(userDetailsService.loadUserByUsername(anyString())).willReturn(mock(UserDetails.class));

        // when & then
        mockMvc.perform(post("/api/v1/conference")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("컨퍼런스 장소 정보는 필수입니다. 공백 이하는 불가능합니다."));


    }

    @DisplayName("컨퍼런스 등록 시 주최자명 입력은 필수입니다.")
    @Test
    @WithMockUser(username = "AUTH1", roles = {"ADMIN"})
    void registerConference_ExceptionOfOrganizer() throws Exception {
        // given
        ConferenceCreateRequest request = new ConferenceCreateRequest(
                "카카오 대규모 IT 행사",
                " ",
                LocalDate.of(3024, 6, 15),
                LocalTime.of(13, 0),
                LocalDate.of(3022, 6, 18),
                LocalTime.of( 18, 0),
                "Seoul, South Korea",
                "로비",
                "IT"
        );

        given(jwtProvider.validateToken(anyString())).willReturn(true);
        given(jwtProvider.getIdentifierFromToken(anyString())).willReturn("AUTH1");
        given(jwtProvider.getRoleTypeFromToken(anyString())).willReturn(RoleType.ADMIN);
        given(userDetailsService.loadUserByUsername(anyString())).willReturn(mock(UserDetails.class));

        // when & then
        mockMvc.perform(post("/api/v1/conference")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer AAAAA.BBBBBBB.CCCCCC")
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("주최자명은 필수입니다. 공백 이하는 불가능합니다."));


    }

    @DisplayName("컨퍼런스 등록 시 유형 입력은 필수입니다.")
    @Test
    @WithMockUser(username = "AUTH1", roles = {"ADMIN"})
    void registerConference_ExceptionOfType() throws Exception {
        // given
        ConferenceCreateRequest request = new ConferenceCreateRequest(
                "카카오 대규모 IT 행사",
                "홍길동",
                LocalDate.of(3024, 6, 15),
                LocalTime.of(13, 0),
                LocalDate.of(3022, 6, 18),
                LocalTime.of( 18, 0),
                "Seoul, South Korea",
                "로비",
                "   "
        );

        given(jwtProvider.validateToken(anyString())).willReturn(true);
        given(jwtProvider.getIdentifierFromToken(anyString())).willReturn("AUTH1");
        given(jwtProvider.getRoleTypeFromToken(anyString())).willReturn(RoleType.ADMIN);
        given(userDetailsService.loadUserByUsername(anyString())).willReturn(mock(UserDetails.class));

        // when & then
        mockMvc.perform(post("/api/v1/conference")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer AAAAA.BBBBBBB.CCCCCC")
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("컨퍼런스 유형은 필수입니다. 공백 이하는 불가능합니다."));


    }

    @DisplayName("컨퍼런스를 수정한다.")
    @Test
    @WithMockUser(username = "AUTH1", roles = {"ADMIN"})
    void updateConference() throws Exception {
        // given
        ConferenceUpdateRequest request = new ConferenceUpdateRequest(
                "Spring Boot Conference 2025",
                "홍길동",
                LocalDate.of(3024, 6, 15),
                LocalTime.of(13, 0),
                LocalDate.of(3022, 6, 18),
                LocalTime.of( 18, 0),
                "Seoul, South Korea",
                "로비",
                "IT"
        );
        ConferenceUpdateResponse response = new ConferenceUpdateResponse(
                "Spring Boot Conference 2025",
                "홍길동",
                LocalDate.of(3024, 6, 15),
                LocalTime.of(13, 0),
                LocalDate.of(3022, 6, 18),
                LocalTime.of( 18, 0),
                "Seoul, South Korea",
                "로비",
                "IT"
        );
        String identifier = "AUTH1";
        given(conferenceService.updateConference(eq(identifier), anyLong(), eq(request))).willReturn(response);
        given(jwtProvider.validateToken(anyString())).willReturn(true);
        given(jwtProvider.getIdentifierFromToken(anyString())).willReturn("AUTH1");
        given(jwtProvider.getRoleTypeFromToken(anyString())).willReturn(RoleType.ADMIN);
        given(userDetailsService.loadUserByUsername(anyString())).willReturn(mock(UserDetails.class));


        // when & then
        mockMvc.perform(patch("/api/v1/conference/{id}", 1L)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.data.name").value(request.name()))
                .andExpect(jsonPath("$.data.startDate").value(request.startDate().format(ISO_LOCAL_DATE)))
                .andExpect(jsonPath("$.data.endDate").value(request.endDate().format(ISO_LOCAL_DATE)))
                .andExpect(jsonPath("$.data.startTime").value(request.startTime().format(DateTimeFormatter.ofPattern("HH:mm"))))
                .andExpect(jsonPath("$.data.endTime").value(request.endTime().format(DateTimeFormatter.ofPattern("HH:mm"))))
                .andExpect(jsonPath("$.data.location").value(request.location()))
                .andExpect(jsonPath("$.data.organizer").value(request.organizer()))
                .andExpect(jsonPath("$.data.type").value(request.type()));


    }

    @DisplayName("컨퍼런스 수정 시 시작 날짜는 반드시 미래여야 한다.")
    @Test
    @WithMockUser(username = "AUTH1", roles = {"ADMIN"})
    void updateConference_ExceptionStartDate() throws Exception {
        // given
        ConferenceUpdateRequest request = new ConferenceUpdateRequest(
                "Spring Boot Conference 2025",
                "홍길동",
                LocalDate.of(2024, 6, 15),
                LocalTime.of(13, 0),
                LocalDate.of(3022, 6, 18),
                LocalTime.of( 18, 0),
                "Seoul, South Korea",
                "로비",
                "IT"
        );

        given(jwtProvider.validateToken(anyString())).willReturn(true);
        given(jwtProvider.getIdentifierFromToken(anyString())).willReturn("AUTH1");
        given(jwtProvider.getRoleTypeFromToken(anyString())).willReturn(RoleType.ADMIN);
        given(userDetailsService.loadUserByUsername(anyString())).willReturn(mock(UserDetails.class));


        // when & then
        mockMvc.perform(patch("/api/v1/conference/{id}", 1L)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("시작 날짜는 미래여야 합니다."))
                .andExpect(jsonPath("$.data").isEmpty());


    }

    @DisplayName("컨퍼런스 수정 시 종료 날짜는 반드시 미래여야 한다.")
    @Test
    @WithMockUser(username = "AUTH1", roles = {"ADMIN"})
    void updateConference_ExceptionEndDate() throws Exception {
        // given
        ConferenceUpdateRequest request = new ConferenceUpdateRequest(
                "Spring Boot Conference 2025",
                "홍길동",
                LocalDate.of(3024, 6, 15),
                LocalTime.of(13, 0),
                LocalDate.of(2024, 6, 18),
                LocalTime.of( 18, 0),
                "Seoul, South Korea",
                "로비",
                "IT"
        );

        given(jwtProvider.validateToken(anyString())).willReturn(true);
        given(jwtProvider.getIdentifierFromToken(anyString())).willReturn("AUTH1");
        given(jwtProvider.getRoleTypeFromToken(anyString())).willReturn(RoleType.ADMIN);
        given(userDetailsService.loadUserByUsername(anyString())).willReturn(mock(UserDetails.class));


        // when & then
        mockMvc.perform(patch("/api/v1/conference/{id}", 1L)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("종료 날짜는 미래여야 합니다."))
                .andExpect(jsonPath("$.data").isEmpty());


    }

}
