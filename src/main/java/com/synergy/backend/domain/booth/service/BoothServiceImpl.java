package com.synergy.backend.domain.booth.service;

import com.synergy.backend.domain.booth.dto.BoothRequestDto;
import com.synergy.backend.domain.booth.dto.BoothResponseDto;
import com.synergy.backend.domain.booth.entity.Booth;
import com.synergy.backend.domain.booth.exception.NotFoundBoothException;
import com.synergy.backend.domain.booth.repository.BoothRepository;
import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.conference.exception.NotFoundConference;
import com.synergy.backend.domain.conference.repository.ConferenceRepository;
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

    @Transactional
    @Override
    public BoothResponseDto createBooth(Long conferenceId, BoothRequestDto request) {
        Conference conference = ifConferenceExists(conferenceId);
        Booth booth = new Booth(
                request.name(),
                request.company(),
                request.location(),
                request.description(),
                conference
        );
        return new BoothResponseDto(boothRepository.save(booth));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<BoothResponseDto> getAllBooths(Long conferenceId, Pageable pageable) {
        return boothRepository.findAllByConferenceId(conferenceId, pageable)
                .map(BoothResponseDto::new);
    }

    @Transactional(readOnly = true)
    @Override
    public BoothResponseDto getBoothById(Long conferenceId, Long id) {
        Booth booth = ifBoothExists(id);
        if (!booth.getConference().getId().equals(conferenceId)) {
            throw new NotFoundBoothException();
        }
        return new BoothResponseDto(booth);
    }

    @Transactional
    @Override
    public BoothResponseDto updateBooth(Long conferenceId, Long id, BoothRequestDto request) {
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

        return new BoothResponseDto(booth);
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
