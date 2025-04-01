package com.synergy.backend.domain.session.entity;

import com.synergy.backend.domain.member.entity.Attendee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class AttendeeSessionTest {

    @DisplayName("QR코드로 인증받은 사용자가 등록됩니다.")
    @Test
    void of(){
     //given
        Session session = mock(Session.class);
        ReflectionTestUtils.setField(session, "id", 1L);
        Attendee attendee = mock(Attendee.class);
        ReflectionTestUtils.setField(attendee, "id", 1L);

     //when
        AttendeeSession attendeeSession = AttendeeSession.of(attendee, session);

        //then
        assertThat(attendeeSession).extracting(
                "attendee", "session").containsExactly(attendee, session);
    }

    @DisplayName("인증받은 사용자가 2개의 질문을 생성합니다.")
    @Test
    void addQuestion(){
     //given
        AttendeeSession attendeeSession = new AttendeeSession();

        SessionQuestion sessionQuestion1 = mock(SessionQuestion.class);
        SessionQuestion sessionQuestion2 = mock(SessionQuestion.class);

        // when
        attendeeSession.addSessionQuestion(sessionQuestion1);
        attendeeSession.addSessionQuestion(sessionQuestion2);

     //then
        assertThat(attendeeSession.getSessionQuestionList()).hasSize(2);
        assertThat(attendeeSession.getSessionQuestionList()).containsExactly(sessionQuestion1, sessionQuestion2);
    }
}