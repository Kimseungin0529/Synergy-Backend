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

import java.time.LocalDate;
import java.time.LocalTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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
                "김승진",
                LocalDate.of(3025, 5, 10),
                LocalTime.of(9, 0),
                LocalDate.of(3025, 5, 11),
                LocalTime.of(18, 0),
                "부천시 오정구 고강동 311-25 1층",
                "A로비",
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
                                fieldWithPath("name").type(JsonFieldType.STRING).description("컨퍼런스명"),
                                fieldWithPath("host").type(JsonFieldType.STRING).description("주최자"),
                                fieldWithPath("startDate").type(JsonFieldType.STRING).description("시작 날짜 (yyyy-MM-dd)"),
                                fieldWithPath("startTime").type(JsonFieldType.STRING).description("시작 시간 (HH:mm)"),
                                fieldWithPath("endDate").type(JsonFieldType.STRING).description("종료 날짜 (yyyy-MM-dd)"),
                                fieldWithPath("endTime").type(JsonFieldType.STRING).description("종료 시간 (HH:mm)"),
                                fieldWithPath("location").type(JsonFieldType.STRING).description("장소"),
                                fieldWithPath("place").type(JsonFieldType.STRING).description("상세 장소"),
                                fieldWithPath("conferenceType").type(JsonFieldType.STRING).description("컨퍼런스 유형 (예: IT, 교육 등)")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.NULL).description("응답 메시지"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("생성된 컨퍼런스 ID")
                        )
                ));
    }

    @DisplayName("컨퍼런스를 수정하는 API")
    @Test
    void updateConference() throws Exception {
        ConferenceUpdateRequest request = new ConferenceUpdateRequest(
                "컨퍼런스명",
                "김승진",
                LocalDate.of(3025, 5, 10),
                LocalTime.of(9, 0),
                LocalDate.of(3025, 5, 11),
                LocalTime.of(18, 0),
                "부천시 오정구 고강동 311-25 1층",
                "A로비",
                "IT"
        );

        given(conferenceService.updateConference(any(), anyLong(), any(ConferenceUpdateRequest.class)))
                .willReturn(new ConferenceUpdateResponse(
                        "컨퍼런스명",
                        "김승진",
                        LocalDate.of(3025, 5, 10),
                        LocalTime.of(9, 0),
                        LocalDate.of(3025, 5, 11),
                        LocalTime.of(18, 0),
                        "부천시 오정구 고강동 311-25 1층",
                        "A로비",
                        "IT"
                ));

        mockMvc.perform(patch("/api/v1/conference/{id}", 1L)
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
                                fieldWithPath("name").type(JsonFieldType.STRING).optional().description("컨퍼런스명"),
                                fieldWithPath("host").type(JsonFieldType.STRING).optional().description("주최자"),
                                fieldWithPath("startDate").type(JsonFieldType.STRING).optional().description("시작 날짜 (yyyy-MM-dd)"),
                                fieldWithPath("startTime").type(JsonFieldType.STRING).optional().description("시작 시간 (HH:mm)"),
                                fieldWithPath("endDate").type(JsonFieldType.STRING).optional().description("종료 날짜 (yyyy-MM-dd)"),
                                fieldWithPath("endTime").type(JsonFieldType.STRING).optional().description("종료 시간 (HH:mm)"),
                                fieldWithPath("location").type(JsonFieldType.STRING).optional().description("장소"),
                                fieldWithPath("place").type(JsonFieldType.STRING).optional().description("상세 장소"),
                                fieldWithPath("conferenceType").type(JsonFieldType.STRING).optional().description("유형 (예: IT)")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.NULL).description("응답 메시지"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING).description("컨퍼런스명"),
                                fieldWithPath("data.organizer").type(JsonFieldType.STRING).description("주최자"),
                                fieldWithPath("data.startDate").type(JsonFieldType.STRING).description("시작 날짜 (yyyy-MM-dd)"),
                                fieldWithPath("data.startTime").type(JsonFieldType.STRING).description("시작 시간 (HH:mm)"),
                                fieldWithPath("data.endDate").type(JsonFieldType.STRING).description("종료 날짜 (yyyy-MM-dd)"),
                                fieldWithPath("data.endTime").type(JsonFieldType.STRING).description("종료 시간 (HH:mm)"),
                                fieldWithPath("data.location").type(JsonFieldType.STRING).description("장소"),
                                fieldWithPath("data.position").type(JsonFieldType.STRING).description("상세 장소"),
                                fieldWithPath("data.type").type(JsonFieldType.STRING).description("유형")
                        )
                ));
    }
}
