package com.synergy.backend.domain.booth.entity;

import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.global.util.file.dto.FileInformationDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

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
        String boothNumber = "101C";
        LocalDate progressDate = LocalDate.of(3024, 4, 15 );
        String boothDescription = "설명A";
        String imageKey = "image-key";
        String imageUrl = "image-url";

        // when
        Booth booth = new Booth(companyName, companyType, boothLocation, boothNumber, progressDate, boothDescription, conference);
        booth.updateImage(new FileInformationDto(imageKey, imageUrl));

        // then
        assertThat(booth).isNotNull();
        assertThat(booth.getCompanyName()).isEqualTo(companyName);
        assertThat(booth.getCompanyType()).isEqualTo(companyType);
        assertThat(booth.getBoothLocation()).isEqualTo(boothLocation);
        assertThat(booth.getBoothNumber()).isEqualTo(boothNumber);
        assertThat(booth.getBoothDescription()).isEqualTo(boothDescription);
        assertThat(booth.getConference()).isEqualTo(conference);
        assertThat(booth.getImageKey()).isEqualTo(imageKey);
        assertThat(booth.getImageUrl()).isEqualTo(imageUrl);
    }

    @DisplayName("부스 정보를 업데이트합니다.")
    @Test
    void updateBoothInfo() {
        // given
        Conference conference = mock(Conference.class);
        LocalDate progressDate = LocalDate.of(3011, 2, 5);
        Booth booth = new Booth("부스A", "회사A", "위치A", "101C", progressDate, "설명A", conference);

        // when
        booth.updateInfo("부스B", "회사B", "위치B", "202C", null,"설명B", "new-image-key", "new-image-url");

        // then
        assertThat(booth.getCompanyName()).isEqualTo("부스B");
        assertThat(booth.getCompanyType()).isEqualTo("회사B");
        assertThat(booth.getBoothLocation()).isEqualTo("위치B");
        assertThat(booth.getBoothNumber()).isEqualTo("202C");
        assertThat(booth.getProgressDate()).isNull();
        assertThat(booth.getBoothDescription()).isEqualTo("설명B");
        assertThat(booth.getImageKey()).isEqualTo("new-image-key");
        assertThat(booth.getImageUrl()).isEqualTo("new-image-url");
    }
}
