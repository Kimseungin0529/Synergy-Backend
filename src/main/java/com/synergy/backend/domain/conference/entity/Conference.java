package com.synergy.backend.domain.conference.entity;


import com.synergy.backend.domain.booth.model.Booth;
import com.synergy.backend.domain.conference.exception.InvalidLocationException;
import com.synergy.backend.domain.conference.exception.InvalidNameException;
import com.synergy.backend.domain.session.entity.Session;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.REMOVE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Conference {

    public static final int MAX_NAME_LENGTH = 30;
    public static final int MAX_ORGANIZER_LENGTH = 10;
    public static final int MAX_COMMON_LENGTH = 50;
    public static final int MAX_LOCATION_LENGTH = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conference_id")
    private Long id;

    @Column(nullable = false, length = MAX_NAME_LENGTH)
    private String name;

    @Embedded
    private TimePeriod period;

    @Column(nullable = false, length = MAX_ORGANIZER_LENGTH)
    private String organizer;

    @Column(nullable = false, length = MAX_LOCATION_LENGTH)
    private String location;

    @Column(nullable = false, length = MAX_COMMON_LENGTH)
    private String type;

    @OneToMany(mappedBy = "conference", cascade = {CascadeType.PERSIST, REMOVE}, orphanRemoval = true)
    private List<Session> sessions = new ArrayList<>();

    @OneToMany(mappedBy = "conference", cascade = {CascadeType.PERSIST, REMOVE}, orphanRemoval = true)
    private List<Booth> booths = new ArrayList<>();

    private Conference(String name, TimePeriod period, String organizer, String location, String type) {
        this.name = name;
        this.period = period;
        this.organizer = organizer;
        this.location = location;
        this.type = type;
    }

    public static Conference of(String name, TimePeriod period, String organizer, String location, String type) {
        if (name.length() > MAX_NAME_LENGTH || name.isBlank()) {
            throw new InvalidNameException();
        }

        if (type.length() > MAX_COMMON_LENGTH || type.isBlank()) {
            throw new InvalidCommonException();
        }

        if (organizer.length() > MAX_ORGANIZER_LENGTH || organizer.isBlank()) {
            throw new InvalidOrganizerException();
        }
        if (location.length() > MAX_LOCATION_LENGTH || location.isBlank()) {
            throw new InvalidLocationException();
        }
        return new Conference(name, period, organizer, location, type);
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateLocation(String location) {
        this.location = location;
    }

    public void updatePeriod(TimePeriod period) {
        this.period = period;
    }

    public void updateOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public void updateType(String type) {
        this.type = type;
    }
}
