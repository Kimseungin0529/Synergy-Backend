package com.synergy.backend.domain.session.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class SessionQuestionTest {

    @DisplayName("QR코드 인증받은 사용자가 질문을 생성합니다.")
    @Test
    void of(){
     //given
        AttendeeSession attendeeSession = mock(AttendeeSession.class);

     //when
        SessionQuestion sessionQuestion = SessionQuestion.of("question1", attendeeSession);

     //then
        assertThat(sessionQuestion).extracting("question").isEqualTo("question1");
    }
}