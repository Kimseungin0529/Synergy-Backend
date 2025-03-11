package com.synergy.backend.domain.booth.entity;

import com.synergy.backend.domain.conference.entity.Conference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Booth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booth_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String company;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false, length = 1000)
    private String description;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "conference_id", nullable = false)
    private Conference conference;

    public Booth(String name, String company, String location, String description, Conference conference) {
        this.name = name;
        this.company = company;
        this.location = location;
        this.description = description;
        this.conference = conference;
    }

    public void updateInfo(String name, String company, String location, String description) {
        this.name = name;
        this.company = company;
        this.location = location;
        this.description = description;
    }
}
