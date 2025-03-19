package com.synergy.backend.docs.conference;

import com.synergy.backend.docs.RestDocsSupport;
import com.synergy.backend.domain.conference.api.ConferenceController;
import com.synergy.backend.domain.conference.dto.requset.ConferenceCreateRequest;
import com.synergy.backend.domain.conference.dto.requset.ConferenceUpdateRequest;
import com.synergy.backend.domain.conference.dto.response.ConferenceCreateResponse;
import com.synergy.backend.domain.conference.dto.response.ConferenceUpdateResponse;
import com.synergy.backend.domain.conference.service.ConferenceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ConferenceControllerDocsTest extends RestDocsSupport {

    private final ConferenceService conferenceService = mock(ConferenceService.class);

    @Override
    protected Object initController() {
        return new ConferenceController(conferenceService);
    }

    @DisplayName("컨퍼런스를 등록하는 API")
    @Test
    void registerConference() throws Exception {
        ConferenceCreateRequest request = new ConferenceCreateRequest(
                "컨퍼런스명",
                LocalDateTime.of(3025, 5, 10, 9, 0),
                LocalDateTime.of(3025, 5, 11, 18, 0),
                "부천시 오정구 고강동 311-25 1층",
                "김승진",
                "IT"
        );

        given(conferenceService.registerConference(any(), any(ConferenceCreateRequest.class)))
                .willReturn(new ConferenceCreateResponse(1L));


        mockMvc.perform(
                        post("/api/v1/conference")
                                .content(objectMapper.writeValueAsString(request))
                                .header("Authorization", "Bearer AAAAA.BBBBBBB.CCCCCC")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("conference-register",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName("Authorization").description("JWT 토큰 (형식: `Bearer {token}`)"),
                                        headerWithName("Content-Type").description("요청 데이터 타입 (application/json)")
                                ),
                                requestFields(
                                        fieldWithPath("name").type(JsonFieldType.STRING)
                                                .description("컨퍼런스명"),
                                        fieldWithPath("startDate").type(JsonFieldType.STRING)
                                                .description("컨퍼런스 시작 날짜 및 시간 (ISO 8601 형식: yyyy-MM-dd'T'HH:mm:ss)"),
                                        fieldWithPath("endDate").type(JsonFieldType.STRING)
                                                .description("컨퍼런스 종료 날짜 및 시간 (ISO 8601 형식: yyyy-MM-dd'T'HH:mm:ss)"),
                                        fieldWithPath("location").type(JsonFieldType.STRING)
                                                .description("컨퍼런스 개최 장소"),
                                        fieldWithPath("organizer").type(JsonFieldType.STRING)
                                                .description("컨퍼런스 주최자"),
                                        fieldWithPath("type").type(JsonFieldType.STRING)
                                                .description("컨퍼런스 유형 (예: IT, 교육, 비즈니스 등)")
                                ),
                                responseFields(
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("결과 상태 (실패 시, 없음)"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER)
                                                .description("HTTP 상태 코드"),
                                        fieldWithPath("message").type(JsonFieldType.NULL)
                                                .description("응답 메시지 (오류 발생 시 포함, 성공 시 null)"),
                                        fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                                .description("생성된 컨퍼런스의 고유 ID")
                                )
                        )
                );
    }

    @DisplayName("컨퍼런스를 수정하는 API")
    @Test
    void updateConference() throws Exception {
        // given
        ConferenceUpdateRequest request = new ConferenceUpdateRequest(
                "Spring Boot Conference 2025",
                LocalDateTime.of(3025, 6, 15, 10, 0),
                LocalDateTime.of(3025, 6, 16, 18, 0),
                "Seoul, South Korea",
                "김승진",
                "IT"
        );


        given(conferenceService.updateConference(any(), anyLong(), any(ConferenceUpdateRequest.class)))
                .willReturn(new ConferenceUpdateResponse(
                        "Spring Boot Conference 2025",
                        LocalDateTime.of(3025, 6, 15, 10, 0),
                        LocalDateTime.of(3025, 6, 16, 18, 0),
                        "Seoul, South Korea",
                        "김승진",
                        "IT"
                ));


        // when & then
        mockMvc.perform(patch("/api/v1/conference/{id}", 1L)
                        //.with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer AAAAA.BBBBBBB.CCCCCC")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("conference-update",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName("Authorization").description("JWT 토큰 (형식: `Bearer {token}`)"),
                                        headerWithName("Content-Type").description("요청 데이터 타입 (application/json)")
                                ),
                                requestFields(
                                        fieldWithPath("name").type(JsonFieldType.STRING)
                                                .optional()
                                                .description("컨퍼런스명"),
                                        fieldWithPath("startDate").type(JsonFieldType.STRING)
                                                .optional()
                                                .description("컨퍼런스 시작 날짜 및 시간 (ISO 8601 형식: yyyy-MM-dd'T'HH:mm:ss)"),
                                        fieldWithPath("endDate").type(JsonFieldType.STRING)
                                                .optional()
                                                .description("컨퍼런스 종료 날짜 및 시간 (ISO 8601 형식: yyyy-MM-dd'T'HH:mm:ss)"),
                                        fieldWithPath("location").type(JsonFieldType.STRING)
                                                .optional()
                                                .description("컨퍼런스 개최 장소"),
                                        fieldWithPath("organizer").type(JsonFieldType.STRING)
                                                .optional()
                                                .description("컨퍼런스 주최자"),
                                        fieldWithPath("type").type(JsonFieldType.STRING)
                                                .optional()
                                                .description("컨퍼런스 유형 (예: IT, 교육, 비즈니스 등)")
                                ),
                                responseFields(
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("결과 상태"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER)
                                                .description("응답 코드 (200: 성공)"),
                                        fieldWithPath("message").type(JsonFieldType.NULL)
                                                .description("응답 메시지 (오류 발생 시 포함, 성공 시 null)"),
                                        fieldWithPath("data.name").type(JsonFieldType.STRING)
                                                .description("컨퍼런스명"),
                                        fieldWithPath("data.startDate").type(JsonFieldType.STRING)
                                                .description("컨퍼런스 시작 날짜 및 시간 (ISO 8601 형식)"),
                                        fieldWithPath("data.endDate").type(JsonFieldType.STRING)
                                                .description("컨퍼런스 종료 날짜 및 시간 (ISO 8601 형식)"),
                                        fieldWithPath("data.location").type(JsonFieldType.STRING)
                                                .description("컨퍼런스 개최 장소"),
                                        fieldWithPath("data.organizer").type(JsonFieldType.STRING)
                                                .description("컨퍼런스 주최자"),
                                        fieldWithPath("data.type").type(JsonFieldType.STRING)
                                                .description("컨퍼런스 유형 (예: IT, 교육, 비즈니스 등)")
                                )
                        )
                );


    }
}
