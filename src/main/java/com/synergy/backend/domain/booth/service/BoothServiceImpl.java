package com.synergy.backend.domain.booth.service;

import com.synergy.backend.domain.booth.dto.BoothRequestDto;
import com.synergy.backend.domain.booth.dto.BoothResponseDto;
import com.synergy.backend.domain.booth.entity.Booth;
import com.synergy.backend.domain.booth.exception.NotFoundBoothException;
import com.synergy.backend.domain.booth.repository.BoothRepository;
import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.conference.repository.ConferenceRepository;
import com.synergy.backend.domain.conference.exception.NotFoundConference;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoothServiceImpl implements BoothService {

    private final BoothRepository boothRepository;
    private final ConferenceRepository conferenceRepository;

    private Booth findBoothById(Long id) {
        return boothRepository.findById(id)
                .orElseThrow(NotFoundBoothException::new);
    }

    private Booth findBoothByIdAndValidateConference(Long conferenceId, Long id) {
        Booth booth = findBoothById(id);
        if (!booth.getConference().getId().equals(conferenceId)) {
            throw new NotFoundBoothException();
        }
        return booth;
    }

    @Override
    public BoothResponseDto getBoothById(Long conferenceId, Long id) {
        return new BoothResponseDto(findBoothByIdAndValidateConference(conferenceId, id));
    }

    @Override
    public Page<BoothResponseDto> getAllBooths(Long conferenceId, Pageable pageable) {
        return boothRepository.findAllByConferenceId(conferenceId, pageable)
                .map(BoothResponseDto::new);
    }

    @Override
    @Transactional
    public BoothResponseDto createBooth(Long conferenceId, BoothRequestDto request) {
        Conference conference = conferenceRepository.findById(conferenceId)
                .orElseThrow(NotFoundConference::new);

        Booth booth = new Booth(
                request.name(),
                request.company(),
                request.location(),
                request.description(),
                conference
        );

        Booth savedBooth = boothRepository.save(booth);
        return new BoothResponseDto(savedBooth);
    }

    @Override
    @Transactional
    public BoothResponseDto updateBooth(Long conferenceId, Long id, BoothRequestDto request) {
        Booth booth = findBoothByIdAndValidateConference(conferenceId, id);

        booth.updateInfo(
                request.name() != null ? request.name() : booth.getName(),
                request.company() != null ? request.company() : booth.getCompany(),
                request.location() != null ? request.location() : booth.getLocation(),
                request.description() != null ? request.description() : booth.getDescription()
        );

        return new BoothResponseDto(booth);
    }

    @Override
    @Transactional
    public void deleteBooth(Long conferenceId, Long id) {
        Booth booth = findBoothByIdAndValidateConference(conferenceId, id);
        boothRepository.delete(booth);
    }
}
