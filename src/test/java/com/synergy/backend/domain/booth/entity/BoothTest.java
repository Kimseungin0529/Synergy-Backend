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
        String companyName = "부스A";
        String companyType = "회사A";
        String boothLocation = "위치A";
        Integer boothNumber = 101;
        String boothDescription = "설명A";
        byte[] image = null;

        // when
        Booth booth = new Booth(companyName, companyType, boothLocation, boothNumber, boothDescription, conference, image);

        // then
        assertThat(booth).isNotNull();
        assertThat(booth.getCompanyName()).isEqualTo(companyName);
        assertThat(booth.getCompanyType()).isEqualTo(companyType);
        assertThat(booth.getBoothLocation()).isEqualTo(boothLocation);
        assertThat(booth.getBoothNumber()).isEqualTo(boothNumber);
        assertThat(booth.getBoothDescription()).isEqualTo(boothDescription);
        assertThat(booth.getConference()).isEqualTo(conference);
    }

    @DisplayName("부스 정보를 업데이트합니다.")
    @Test
    void updateBoothInfo() {
        // given
        Conference conference = mock(Conference.class);
        Booth booth = new Booth("부스A", "회사A", "위치A", 101, "설명A", conference, null);

        // when
        booth.updateInfo("부스B", "회사B", "위치B", 202, "설명B", null);

        // then
        assertThat(booth.getCompanyName()).isEqualTo("부스B");
        assertThat(booth.getCompanyType()).isEqualTo("회사B");
        assertThat(booth.getBoothLocation()).isEqualTo("위치B");
        assertThat(booth.getBoothNumber()).isEqualTo(202);
        assertThat(booth.getBoothDescription()).isEqualTo("설명B");
    }
}
