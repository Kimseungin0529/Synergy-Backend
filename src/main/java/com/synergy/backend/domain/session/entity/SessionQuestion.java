package com.synergy.backend.domain.session.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SessionQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    @ManyToOne
    @JoinColumn(name = "attendeeSessionId")
    private AttendeeSession attendeeSession;

    @Builder
    public SessionQuestion(String question, AttendeeSession attendeeSession) {
        this.question = question;
        this.attendeeSession = attendeeSession;
    }

    public static SessionQuestion of(String question, AttendeeSession attendeeSession) {
        return SessionQuestion.builder()
                .question(question)
                .attendeeSession(attendeeSession)
                .build();
    }
}
