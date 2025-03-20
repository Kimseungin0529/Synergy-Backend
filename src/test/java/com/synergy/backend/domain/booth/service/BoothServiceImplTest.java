package com.synergy.backend.domain.booth.service;

import com.synergy.backend.domain.booth.dto.BoothRequestDto;
import com.synergy.backend.domain.booth.dto.BoothResponseDto;
import com.synergy.backend.domain.booth.entity.Booth;
import com.synergy.backend.domain.booth.exception.NotFoundBoothException;
import com.synergy.backend.domain.booth.repository.BoothRepository;
import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.conference.repository.ConferenceRepository;
import com.synergy.backend.domain.qrCode.service.QrService;
import com.synergy.backend.global.common.ApiResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;
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
    //@Test
    void createBooth() {
        // given
        Long conferenceId = 1L;
        BoothRequestDto request = new BoothRequestDto("부스A", "회사A", "위치A", "설명A", "도메인주소A");
        Conference conference = mock(Conference.class);
        when(conferenceRepository.findById(conferenceId)).thenReturn(Optional.of(conference));
        Booth booth = new Booth(request.name(), request.company(), request.location(), request.description(), conference);
        when(boothRepository.save(any(Booth.class))).thenReturn(booth);

        // when
        ApiResponse<BoothResponseDto> response = boothService.createBooth(conferenceId, request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.data()).isNotNull();
        assertThat(response.data().name()).isEqualTo("부스A");
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
        ApiResponse<Page<BoothResponseDto>> response = boothService.getAllBooths(conferenceId, pageable);

        // then
        assertThat(response).isNotNull();
        assertThat(response.data().getContent().size()).isEqualTo(1);
    }

    @DisplayName("부스를 ID로 조회합니다.")
    @Test
    void getBoothById() {
        // given
        Long conferenceId = 1L;
        Long boothId = 1L;
        Conference conference = mock(Conference.class);
        when(conference.getId()).thenReturn(conferenceId);
        Booth booth = new Booth("부스A", "회사A", "위치A", "설명A", conference);
        when(boothRepository.findById(boothId)).thenReturn(Optional.of(booth));

        // when
        ApiResponse<BoothResponseDto> response = boothService.getBoothById(conferenceId, boothId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.data()).isNotNull();
        assertThat(response.data().name()).isEqualTo("부스A");
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
        BoothRequestDto request = new BoothRequestDto("부스B", "회사B", "위치B", "설명B", "도메인주소B");
        Conference conference = mock(Conference.class);
        when(conference.getId()).thenReturn(conferenceId);
        Booth booth = new Booth("부스A", "회사A", "위치A", "설명A", conference);
        when(boothRepository.findById(boothId)).thenReturn(Optional.of(booth));

        // when
        ApiResponse<BoothResponseDto> response = boothService.updateBooth(conferenceId, boothId, request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.data().name()).isEqualTo("부스B");
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
        ApiResponse<Void> response = boothService.deleteBooth(conferenceId, boothId);

        // then
        assertThat(response).isNotNull();
        verify(boothRepository, times(1)).delete(booth);
    }
}
