package com.synergy.backend.domain.conference.service;


import com.synergy.backend.domain.conference.dto.requset.ConferenceCreateRequest;
import com.synergy.backend.domain.conference.dto.requset.ConferenceUpdateRequest;
import com.synergy.backend.domain.conference.dto.response.ConferenceCreateResponse;
import com.synergy.backend.domain.conference.dto.response.ConferenceUpdateResponse;
import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.conference.entity.TimePeriod;
import com.synergy.backend.domain.conference.exception.NotFoundConference;
import com.synergy.backend.domain.conference.repository.ConferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConferenceServiceImpl implements ConferenceService {
    private final ConferenceRepository conferenceRepository;

    @Transactional
    @Override
    public ConferenceCreateResponse registerConference(ConferenceCreateRequest request) {
        TimePeriod timePeriod = TimePeriod.of(request.startDate(), request.endDate());
        Conference conference = Conference.of(request.name(), timePeriod, request.organizer(), request.location(), request.type());
        Conference savedConference = conferenceRepository.save(conference);

        return ConferenceCreateResponse.from(savedConference);
    }

    @Transactional
    @Override
    public ConferenceUpdateResponse updateConference(Long conferenceId, ConferenceUpdateRequest request) {
        Conference findConference = conferenceRepository.findById(conferenceId)
                .orElseThrow(NotFoundConference::new);
        applyUpdatesToConference(request, findConference);

        return ConferenceUpdateResponse.from(findConference); // 반영된 정보 반환
    }

    @Override
    public ConferenceDetailResponse findConference(Long conferenceId) {
        return null;
    }

    private void applyUpdatesToConference(ConferenceUpdateRequest request, Conference findConference) {
        Optional.ofNullable(request.name()).ifPresent(findConference::updateName);
        Optional.ofNullable(request.location()).ifPresent(findConference::updateLocation);
        Optional.ofNullable(request.organizer()).ifPresent(findConference::updateOrganizer);
        Optional.ofNullable(request.type()).ifPresent(findConference::updateType);

        Optional<LocalDateTime> optionalStartTime = Optional.ofNullable(request.startTime());
        Optional<LocalDateTime> optionalEndTime = Optional.ofNullable(request.endTime());
        if (optionalStartTime.isPresent() && optionalEndTime.isPresent()) {
            findConference.updatePeriod(TimePeriod.of(optionalStartTime.get(), optionalEndTime.get()));
        }

    }
}
