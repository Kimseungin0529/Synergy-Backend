package com.synergy.backend.domain.member.service.spring;

import com.synergy.backend.domain.member.api.dto.request.AttendeeFilterRequest;
import com.synergy.backend.domain.member.api.dto.response.AttendeeListResponse;
import com.synergy.backend.domain.member.entity.Recruiter;
import com.synergy.backend.domain.member.repository.AttendeeRepository;
import com.synergy.backend.domain.member.repository.RecruiterRepository;
import com.synergy.backend.domain.member.service.RecruiterService;
import com.synergy.backend.module.IntegrationSupportTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class RecruiterServiceImplTest extends IntegrationSupportTest {

    @Autowired
    RecruiterService recruiterService;
    @Autowired
    RecruiterRepository recruiterRepository;
    @Autowired
    private AttendeeRepository attendeeRepository;

    @DisplayName("필터 조건에 맞는 참가자 리스트를 페이징하여 반환한다.")
    @Test
    void getAttendeesBy() {
        // given
        Recruiter recruiter = Recruiter.of("AUTH_TEST");
        Recruiter savedRecruiter = recruiterRepository.save(recruiter);
        /**
         * data-test.sql 로 테스트 진행
         * 개인적으로는 sql 파일과 이동하면서 매칭해야 하기에 보기 어려운 거 같습니다.
         *
         */

        Pageable pageable = PageRequest.of(0, 10);
        List<String> desiredJobPositions = List.of("백엔드 개발자", "그래픽 디자이너");
        String educationLevel = null;
        String ageGroup = "20~24세 이하";
        String experienceLevel = "1~2년 이하";
        List<String> regions = List.of();
        AttendeeFilterRequest requestCondition = AttendeeFilterRequest.of(desiredJobPositions, educationLevel, ageGroup,
                experienceLevel, regions);

        // when
        AttendeeListResponse result = recruiterService.getAttendeesBy(pageable, savedRecruiter.getId(),
                requestCondition);

        // then
        assertThat(result)
                .extracting("currentPageNumber", "totalPages", "totalElements", "pageSize")
                .containsExactly(pageable.getPageNumber(), 1, 1L, pageable.getPageSize());

        assertThat(result.getList())
                .hasSize(1)
                .extracting("name", "desiredJobPosition", "experienceLevel")
                .containsExactlyInAnyOrder(
                        tuple("김지원", "백엔드 개발자", "1~2년 이하")
                );

    }

}
