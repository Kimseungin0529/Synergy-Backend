package com.synergy.backend.domain.member.repository.spring;

import com.synergy.backend.domain.member.api.dto.AttendeeFilterRequest;
import com.synergy.backend.domain.member.api.dto.AttendeeSimpleResponseDto;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.Recruiter;
import com.synergy.backend.domain.member.repository.AttendeeRepository;
import com.synergy.backend.domain.member.repository.RecruiterRepository;
import com.synergy.backend.global.config.QuerydslConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(QuerydslConfig.class)
class AttendeeRepositoryTest {

    @Autowired
    AttendeeRepository attendeeRepository;

    @Autowired
    RecruiterRepository recruiterRepository;

    @DisplayName("각종 필터를 기준으로 페이징 조회를 반환합니다.")
    @Test
    void searchPageAttendeesBy() {
        // given
        Recruiter savedRecruiter = recruiterRepository.save(Recruiter.of("RC_TEST"));
        Pageable pageable = PageRequest.of(0, 10);

        List<String> occupations = List.of("백엔드 개발자", "프론트엔드 개발자");
        String educationLevel = null;
        String ageGroup = null;
        String experienceLevel = null;
        List<String> regions = List.of();
        AttendeeFilterRequest requestCondition = AttendeeFilterRequest.of(occupations, educationLevel, ageGroup, experienceLevel, regions);

        // when
        Page<AttendeeSimpleResponseDto> result = attendeeRepository.searchPageAttendeesBy(pageable, savedRecruiter.getId(), requestCondition);


        // then
        assertThat(result.getNumber()).isEqualTo(0);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getTotalElements()).isEqualTo(4L);

        List<AttendeeSimpleResponseDto> content = result.getContent();
        assertThat(content)
                .extracting("name", "occupation", "experienceLevel", "techStacks")
                .containsExactlyInAnyOrder(
                        tuple("김지원", "백엔드 개발자", "1~2년 이하", "Java, AWS, Spring Boot, MySQL, Docker, JPA, github-actions, SonarQube, Redis, junit5, Mockito, Git"),
                        tuple("박시형", "프론트엔드 개발자", "신입", "Go, C++"),
                        tuple("이다영", "프론트엔드 개발자", "신입", "Go, C++"),
                        tuple("김다혜", "프론트엔드 개발자", "신입", "Git, Docker")
                );


    }

}