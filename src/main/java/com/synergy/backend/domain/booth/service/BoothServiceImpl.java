package com.synergy.backend.domain.booth.service;

import com.google.zxing.WriterException;
import com.synergy.backend.domain.booth.dto.BoothDetailResponseDto;
import com.synergy.backend.domain.booth.dto.BoothRequestDto;
import com.synergy.backend.domain.booth.dto.BoothResponseDto;
import com.synergy.backend.domain.booth.entity.Booth;
import com.synergy.backend.domain.booth.exception.NotFoundBoothException;
import com.synergy.backend.domain.booth.repository.BoothRepository;
import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.conference.exception.NotFoundConference;
import com.synergy.backend.domain.conference.repository.ConferenceRepository;
import com.synergy.backend.domain.qrCode.service.QrService;
import com.synergy.backend.global.util.file.dto.FileInformationDto;
import com.synergy.backend.global.util.file.util.FileS3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoothServiceImpl implements BoothService {

    private final BoothRepository boothRepository;
    private final ConferenceRepository conferenceRepository;
    private final FileS3Util fileS3Util;
    private final QrService qrService;

    @Transactional
    @Override
    public BoothDetailResponseDto createBooth(Long conferenceId, BoothRequestDto request, MultipartFile imageFile) throws WriterException {
        Conference conference = ifConferenceExists(conferenceId);

        Booth booth = new Booth(
                request.companyName(),
                request.companyType(),
                request.boothLocation(),
                request.boothNumber(),
                request.progressDate(),
                request.boothDescription(),
                conference
        );
        Booth savedBooth = boothRepository.save(booth);
        String secretCode = UUID.randomUUID().toString();
        savedBooth.updateSecretCode(secretCode);

        String url = "/booth/" + savedBooth.getId();
        byte[] qrCode = qrService.generateQRCode(url, secretCode);
        FileInformationDto qrInfo = fileS3Util.uploadQRCode(qrCode, savedBooth.getCompanyName());
        savedBooth.updateQr(qrInfo);

        FileInformationDto imageInfo = fileS3Util.uploadFile(imageFile);
        booth.updateImage(imageInfo);
        boothRepository.save(booth);

        return new BoothDetailResponseDto(booth);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<BoothResponseDto> getAllBooths(Long conferenceId, Pageable pageable) {
        return boothRepository.findAllByConferenceId(conferenceId, pageable)
                .map(BoothResponseDto::of);
    }

    @Transactional(readOnly = true)
    @Override
    public BoothDetailResponseDto getBoothById(Long conferenceId, Long id) {
        Booth booth = ifBoothExists(id);
        if (!booth.getConference().getId().equals(conferenceId)) {
            throw new NotFoundBoothException();
        }
        return new BoothDetailResponseDto(booth);
    }

    @Transactional
    @Override
    public BoothDetailResponseDto updateBooth(Long conferenceId, Long id, BoothRequestDto request, MultipartFile imageFile) {
        Booth booth = ifBoothExists(id);
        if (!booth.getConference().getId().equals(conferenceId)) {
            throw new NotFoundConference();
        }

        FileInformationDto imageInfo = (imageFile != null) ? fileS3Util.uploadFile(imageFile) : null;

        booth.updateInfo(
                request.companyName() != null ? request.companyName() : booth.getCompanyName(),
                request.companyType() != null ? request.companyType() : booth.getCompanyType(),
                request.boothLocation() != null ? request.boothLocation() : booth.getBoothLocation(),
                request.boothNumber() != null ? request.boothNumber() : booth.getBoothNumber(),
                request.progressDate() != null ? request.progressDate() : booth.getProgressDate(),
                request.boothDescription() != null ? request.boothDescription() : booth.getBoothDescription(),
                imageInfo != null ? imageInfo.fileKey() : booth.getImageKey(),
                imageInfo != null ? imageInfo.accessUrl() : booth.getImageUrl()
        );

        return new BoothDetailResponseDto(booth);
    }

    @Transactional
    @Override
    public void deleteBooth(Long conferenceId, Long id) {
        Booth booth = ifBoothExists(id);
        if (!booth.getConference().getId().equals(conferenceId)) {
            throw new NotFoundBoothException();
        }
        boothRepository.delete(booth);
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
