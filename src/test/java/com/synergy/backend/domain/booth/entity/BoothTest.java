package com.synergy.backend.domain.booth.entity;

import com.synergy.backend.domain.conference.entity.Conference;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;

public class BoothTest {

    @DisplayName("부스를 생성합니다.")
    @Test
    void createBooth() {
        // given
        Conference conference = mock(Conference.class);
        String name = "부스A";
        String company = "회사A";
        String location = "위치A";
        String description = "설명A";

        // when
        Booth booth = new Booth(name, company, location, description, conference);

        // then
        assertThat(booth).isNotNull();
        assertThat(booth.getName()).isEqualTo(name);
        assertThat(booth.getCompany()).isEqualTo(company);
        assertThat(booth.getLocation()).isEqualTo(location);
        assertThat(booth.getDescription()).isEqualTo(description);
        assertThat(booth.getConference()).isEqualTo(conference);
    }

    @DisplayName("부스 정보를 업데이트합니다.")
    @Test
    void updateBoothInfo() {
        // given
        Conference conference = mock(Conference.class);
        Booth booth = new Booth("부스A", "회사A", "위치A", "설명A", conference);

        // when
        booth.updateInfo("부스B", "회사B", "위치B", "설명B");

        // then
        assertThat(booth.getName()).isEqualTo("부스B");
        assertThat(booth.getCompany()).isEqualTo("회사B");
        assertThat(booth.getLocation()).isEqualTo("위치B");
        assertThat(booth.getDescription()).isEqualTo("설명B");
    }
}
