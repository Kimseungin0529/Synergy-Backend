package com.synergy.backend.domain.session.entity;

import com.synergy.backend.domain.member.entity.Attendee;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "attendeeSession", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<SessionQuestion> sessionQuestionList = new ArrayList<>();

    @Builder
    public AttendeeSession(Attendee attendee, Session session) {
        this.attendee = attendee;
        this.session = session;
    }

    public static AttendeeSession of(Attendee attendee, Session session) {
        return AttendeeSession.builder()
                .attendee(attendee)
                .session(session)
                .build();
    }

    public void addSessionQuestion(SessionQuestion sessionQuestion) {
        this.sessionQuestionList.add(sessionQuestion);
    }
}
