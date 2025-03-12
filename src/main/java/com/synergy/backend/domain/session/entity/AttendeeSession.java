package com.synergy.backend.domain.session.entity;

import com.synergy.backend.domain.member.entity.Attendee;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class AttendeeSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 10)
    @Column(nullable = false, length = 300)
    private String question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendee_id")
    private Attendee attendee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private Session session;

    @Builder
    public AttendeeSession(Attendee attendee, Session session, String question) {
        this.attendee = attendee;
        this.session = session;
        this.question = question;
    }

    public static AttendeeSession of(Attendee attendee, Session session, String question) {
        return AttendeeSession.builder()
                .attendee(attendee)
                .session(session)
                .question(question)
                .build();
    }
}
