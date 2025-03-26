package com.synergy.backend.domain.conference.service;


import com.synergy.backend.domain.conference.dto.requset.ConferenceCreateRequest;
import com.synergy.backend.domain.conference.dto.requset.ConferenceUpdateRequest;
import com.synergy.backend.domain.conference.dto.response.ConferenceCreateResponse;
import com.synergy.backend.domain.conference.dto.response.ConferenceUpdateResponse;
import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.conference.entity.TimePeriod;
import com.synergy.backend.domain.conference.exception.NotFoundConference;
import com.synergy.backend.domain.conference.repository.ConferenceRepository;
import com.synergy.backend.domain.member.entity.Admin;
import com.synergy.backend.domain.member.exception.AccessDeniedException;
import com.synergy.backend.domain.member.exception.NotFoundUserException;
import com.synergy.backend.domain.member.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConferenceServiceImpl implements ConferenceService {
    private final ConferenceRepository conferenceRepository;
    private final AdminRepository adminRepository;

    @Transactional
    @Override
    public ConferenceCreateResponse registerConference(String identifier, ConferenceCreateRequest request) {
        Admin findAdmin = adminRepository.findByAdminAuthCode(identifier).orElseThrow(NotFoundUserException::new);
        TimePeriod timePeriod = TimePeriod.of(request.startDate(), request.endDate(), request.startTime(), request.endTime());
        Conference conference = Conference.of(request.name(), timePeriod, request.host(), request.location(), request.place(), request.conferenceType());
        Conference savedConference = conferenceRepository.save(conference);

        findAdmin.addConference(conference);

        return ConferenceCreateResponse.from(savedConference);
    }

    @Transactional
    @Override
    public ConferenceUpdateResponse updateConference(String identifier, Long conferenceId, ConferenceUpdateRequest request) {
        Admin findAdmin = adminRepository.findByAdminAuthCode(identifier).orElseThrow(NotFoundUserException::new);
        Conference findConference = conferenceRepository.findById(conferenceId)
                .orElseThrow(NotFoundConference::new);
        // 해당 컨퍼런스를 등록한 관리자가 수정을 하려고 하는지 검증 (다른 관리자가 만든 컨퍼런스에 접근해서는 안 된다.)
        if (!findAdmin.getConferences().contains(findConference)) {
            throw new AccessDeniedException();
        }
        applyUpdatesToConference(request, findConference);

        return ConferenceUpdateResponse.from(findConference); // 반영된 정보 반환
    }

    @Override
    public ConferenceDetailResponse findConference(Long conferenceId) {
        return null;
    }

    private void applyUpdatesToConference(ConferenceUpdateRequest request, Conference findConference) {
        updateInform(request, findConference);

        updateDateTimeInform(request, findConference);

    }

    private void updateDateTimeInform(ConferenceUpdateRequest request, Conference findConference) {
        LocalDate currentStartDate = findConference.getPeriod().getStartDate();
        LocalDate currentEndDate = findConference.getPeriod().getEndDate();
        LocalTime currentStartTime = findConference.getPeriod().getStartTime();
        LocalTime currentEndTime = findConference.getPeriod().getEndTime();

        LocalDate newStartDate = Optional.ofNullable(request.startDate()).orElse(currentStartDate);
        LocalDate newEndDate = Optional.ofNullable(request.endDate()).orElse(currentEndDate);
        LocalTime newStartTime = Optional.ofNullable(request.startTime()).orElse(currentStartTime);
        LocalTime newEndTime = Optional.ofNullable(request.endTime()).orElse(currentEndTime);

        findConference.updatePeriod(TimePeriod.of(newStartDate, newEndDate, newStartTime, newEndTime));
    }

    private void updateInform(ConferenceUpdateRequest request, Conference findConference) {
        Optional.ofNullable(request.name()).ifPresent(findConference::updateName);
        Optional.ofNullable(request.location()).ifPresent(findConference::updateLocation);
        Optional.ofNullable(request.host()).ifPresent(findConference::updateOrganizer);
        Optional.ofNullable(request.conferenceType()).ifPresent(findConference::updateType);
        Optional.ofNullable(request.place()).ifPresent(findConference::updatePosition);
    }
}
