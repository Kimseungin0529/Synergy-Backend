package com.synergy.backend.domain.booth.service;

import com.google.zxing.WriterException;
import com.synergy.backend.domain.booth.dto.BoothRequestDto;
import com.synergy.backend.domain.booth.dto.BoothResponseDto;
import com.synergy.backend.domain.booth.entity.Booth;
import com.synergy.backend.domain.booth.exception.NotFoundBoothException;
import com.synergy.backend.domain.booth.repository.BoothRepository;
import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.conference.exception.NotFoundConference;
import com.synergy.backend.domain.conference.repository.ConferenceRepository;
import com.synergy.backend.domain.qrCode.exception.NotGenerateQRCodeException;
import com.synergy.backend.domain.qrCode.service.QrService;
import com.synergy.backend.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoothServiceImpl implements BoothService {

    private final BoothRepository boothRepository;
    private final ConferenceRepository conferenceRepository;
    private final QrService qrService;

    @Transactional
    @Override
    public ApiResponse<BoothResponseDto> createBooth(Long conferenceId, BoothRequestDto request) {
        Conference conference = ifConferenceExists(conferenceId);
        Booth booth = new Booth(
                request.name(),
                request.company(),
                request.location(),
                request.description(),
                conference
        );

        booth = boothRepository.save(booth);

        String qrCodeUrl = "https://";
        try {
            byte[] qrCode = qrService.generateQRCode(qrCodeUrl);
            booth.setQrCode(qrCode);
        } catch (WriterException e) {
            throw new NotGenerateQRCodeException();
        }

        return ApiResponse.ok(new BoothResponseDto(booth), 201);
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse<Page<BoothResponseDto>> getAllBooths(Long conferenceId, Pageable pageable) {
        return ApiResponse.ok(boothRepository.findAllByConferenceId(conferenceId, pageable)
                .map(BoothResponseDto::new), 200);
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse<BoothResponseDto> getBoothById(Long conferenceId, Long id) {
        Booth booth = ifBoothExists(id);
        if (!booth.getConference().getId().equals(conferenceId)) {
            throw new NotFoundBoothException();
        }
        return ApiResponse.ok(new BoothResponseDto(booth), 200);
    }

    @Transactional
    @Override
    public ApiResponse<BoothResponseDto> updateBooth(Long conferenceId, Long id, BoothRequestDto request) {
        Booth booth = ifBoothExists(id);
        if (!booth.getConference().getId().equals(conferenceId)) {
            throw new NotFoundBoothException();
        }

        booth.updateInfo(
                request.name() != null ? request.name() : booth.getName(),
                request.company() != null ? request.company() : booth.getCompany(),
                request.location() != null ? request.location() : booth.getLocation(),
                request.description() != null ? request.description() : booth.getDescription()
        );

        return ApiResponse.ok(new BoothResponseDto(booth), 200);
    }

    @Transactional
    @Override
    public ApiResponse<Void> deleteBooth(Long conferenceId, Long id) {
        Booth booth = ifBoothExists(id);
        if (!booth.getConference().getId().equals(conferenceId)) {
            throw new NotFoundBoothException();
        }
        boothRepository.delete(booth);
        return ApiResponse.ok(null, 204);
    }

    private Conference ifConferenceExists(Long conferenceId) {
        return conferenceRepository.findById(conferenceId)
                .orElseThrow(NotFoundConference::new);
    }

    private Booth ifBoothExists(Long boothId) {
        return boothRepository.findById(boothId)
                .orElseThrow(NotFoundBoothException::new);
    }
}
