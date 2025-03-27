package com.synergy.backend.domain.booth.service;

import com.google.zxing.WriterException;
import com.synergy.backend.domain.booth.dto.BoothDetailResponseDto;
import com.synergy.backend.domain.booth.dto.BoothRequestDto;
import com.synergy.backend.domain.booth.dto.BoothResponseDto;
import com.synergy.backend.domain.booth.entity.Booth;
import com.synergy.backend.domain.booth.exception.NotFoundBoothException;
import com.synergy.backend.domain.booth.repository.BoothRepository;
import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.conference.repository.ConferenceRepository;
import com.synergy.backend.domain.qrCode.service.QrService;
import com.synergy.backend.global.util.file.dto.FileInformationDto;
import com.synergy.backend.global.util.file.util.FileS3Util;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoothServiceImplTest {

    @Mock
    private BoothRepository boothRepository;

    @Mock
    private ConferenceRepository conferenceRepository;

    @Mock
    private QrService qrService;

    @Mock
    private FileS3Util fileS3Util;

    @Mock
    private MultipartFile mockImageFile;

    @InjectMocks
    private BoothServiceImpl boothService;

    @DisplayName("부스를 생성합니다.")
    @Test
    void createBooth() throws WriterException {
        // given
        Long conferenceId = 1L;
        LocalDate progressDate = LocalDate.of(3012, 11, 11);
        BoothRequestDto request = new BoothRequestDto("부스A", "회사A", progressDate, "위치A", "101", "설명A");
        Conference conference = mock(Conference.class);

        Booth booth = new Booth(request.companyName(), request.companyType(), request.boothLocation(), request.boothNumber(), request.progressDate(), request.boothDescription(), conference);
        Booth spyBooth = spy(booth);
        given(spyBooth.getId()).willReturn(10L);

        FileInformationDto qrDto = new FileInformationDto("qr-key", "qr-url");
        FileInformationDto imageDto = new FileInformationDto("image-key", "image-url");

        given(conferenceRepository.findById(conferenceId)).willReturn(Optional.of(conference));
        given(boothRepository.save(any(Booth.class))).willReturn(spyBooth);
        given(qrService.generateQRCode(anyString(), anyString())).willReturn(new byte[]{1, 2, 3});
        given(fileS3Util.uploadQRCode(any(), anyString())).willReturn(qrDto);
        given(fileS3Util.uploadFile(any())).willReturn(imageDto);

        // when
        BoothDetailResponseDto response = boothService.createBooth(conferenceId, request, mockImageFile);

        // then
        assertThat(response).isNotNull();
        assertThat(response.companyName()).isEqualTo("부스A");
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

        LocalDate progressDate = LocalDate.of(3012, 11, 11);
        Booth booth = new Booth("부스A", "회사A", "위치A", "101", progressDate, "설명A", conference);

        when(conference.getId()).thenReturn(conferenceId);
        when(boothRepository.findById(boothId)).thenReturn(Optional.of(booth));

        // when
        BoothDetailResponseDto response = boothService.getBoothById(conferenceId, boothId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.companyName()).isEqualTo("부스A");
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
        LocalDate progressDate = LocalDate.of(3012, 11, 11);
        BoothRequestDto request = new BoothRequestDto("부스B", "회사B", progressDate, "위치B", "202", "설명B");
        Conference conference = mock(Conference.class);
        when(conference.getId()).thenReturn(conferenceId);
        Booth booth = new Booth("부스A", "회사A", "위치A", "101", progressDate, "설명A", conference);
        when(boothRepository.findById(boothId)).thenReturn(Optional.of(booth));

        // when
        BoothDetailResponseDto response = boothService.updateBooth(conferenceId, boothId, request, mockImageFile);

        // then
        assertThat(response).isNotNull();
        assertThat(response.companyName()).isEqualTo("부스B");
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
