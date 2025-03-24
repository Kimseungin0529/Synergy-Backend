package com.synergy.backend.domain.booth.service;

import com.synergy.backend.domain.booth.dto.BoothRequestDto;
import com.synergy.backend.domain.booth.dto.BoothResponseDto;
import com.synergy.backend.domain.booth.entity.Booth;
import com.synergy.backend.domain.booth.exception.NotFoundBoothException;
import com.synergy.backend.domain.booth.repository.BoothRepository;
import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.conference.repository.ConferenceRepository;
import com.synergy.backend.domain.qrCode.service.QrService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoothServiceImplTest {

    @Mock
    private BoothRepository boothRepository;

    @Mock
    private ConferenceRepository conferenceRepository;

    @Mock
    private QrService qrService;

    @InjectMocks
    private BoothServiceImpl boothService;

    @DisplayName("부스를 생성합니다.")
    @Test
    void createBooth() {
        // given
        Long conferenceId = 1L;
        BoothRequestDto request = new BoothRequestDto("부스A", "회사A", "위치A", 101, "설명A", null);
        Conference conference = mock(Conference.class);
        when(conferenceRepository.findById(conferenceId)).thenReturn(Optional.of(conference));
        Booth booth = new Booth(request.companyName(), request.companyType(), request.boothLocation(), request.boothNumber(), request.boothDescription(), conference, request.image());
        when(boothRepository.save(any(Booth.class))).thenReturn(booth);

        // when
        BoothResponseDto response = boothService.createBooth(conferenceId, request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.companyName()).isEqualTo("부스A");
        assertThat(response.companyType()).isEqualTo("회사A");
        assertThat(response.boothLocation()).isEqualTo("위치A");
        assertThat(response.boothNumber()).isEqualTo(101);
        assertThat(response.boothDescription()).isEqualTo("설명A");
    }

    @DisplayName("컨퍼런스 ID로 모든 부스를 조회합니다.")
    @Test
    void getAllBooths() {
        // given
        Long conferenceId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Booth booth = mock(Booth.class);
        Page<Booth> booths = new PageImpl<>(List.of(booth));
        when(boothRepository.findAllByConferenceId(conferenceId, pageable)).thenReturn(booths);

        // when
        Page<BoothResponseDto> response = boothService.getAllBooths(conferenceId, pageable);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getContent().size()).isEqualTo(1);
    }

    @DisplayName("부스를 ID로 조회합니다.")
    @Test
    void getBoothById() {
        // given
        Long conferenceId = 1L;
        Long boothId = 1L;
        Conference conference = mock(Conference.class);
        when(conference.getId()).thenReturn(conferenceId);
        Booth booth = new Booth("부스A", "회사A", "위치A", 101, "설명A", conference, null);
        when(boothRepository.findById(boothId)).thenReturn(Optional.of(booth));

        // when
        BoothResponseDto response = boothService.getBoothById(conferenceId, boothId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.companyName()).isEqualTo("부스A");
        assertThat(response.companyType()).isEqualTo("회사A");
        assertThat(response.boothLocation()).isEqualTo("위치A");
        assertThat(response.boothNumber()).isEqualTo(101);
        assertThat(response.boothDescription()).isEqualTo("설명A");
    }

    @DisplayName("존재하지 않는 부스를 조회하면 예외가 발생합니다.")
    @Test
    void getBoothById_NotFound() {
        // given
        Long conferenceId = 1L;
        Long boothId = 1L;
        when(boothRepository.findById(boothId)).thenReturn(Optional.empty());

        // when
        Throwable thrown = catchThrowable(() -> boothService.getBoothById(conferenceId, boothId));

        // then
        assertThat(thrown).isInstanceOf(NotFoundBoothException.class);
    }

    @DisplayName("부스 정보를 업데이트합니다.")
    @Test
    void updateBooth() {
        // given
        Long conferenceId = 1L;
        Long boothId = 1L;
        BoothRequestDto request = new BoothRequestDto("부스B", "회사B", "위치B", 202, "설명B", null);
        Conference conference = mock(Conference.class);
        when(conference.getId()).thenReturn(conferenceId);
        Booth booth = new Booth("부스A", "회사A", "위치A", 101, "설명A", conference, null);
        when(boothRepository.findById(boothId)).thenReturn(Optional.of(booth));

        // when
        BoothResponseDto response = boothService.updateBooth(conferenceId, boothId, request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.companyName()).isEqualTo("부스B");
        assertThat(response.companyType()).isEqualTo("회사B");
        assertThat(response.boothLocation()).isEqualTo("위치B");
        assertThat(response.boothNumber()).isEqualTo(202);
        assertThat(response.boothDescription()).isEqualTo("설명B");
    }

    @DisplayName("부스를 삭제합니다.")
    @Test
    void deleteBooth() {
        // given
        Long conferenceId = 1L;
        Long boothId = 1L;
        Booth booth = mock(Booth.class);
        when(booth.getConference()).thenReturn(mock(Conference.class));
        when(booth.getConference().getId()).thenReturn(conferenceId);
        when(boothRepository.findById(boothId)).thenReturn(Optional.of(booth));

        // when
        boothService.deleteBooth(conferenceId, boothId);

        // then
        verify(boothRepository, times(1)).delete(booth);
    }
}
