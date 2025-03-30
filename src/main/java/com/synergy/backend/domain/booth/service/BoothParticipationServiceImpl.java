package com.synergy.backend.domain.booth.service;

import com.synergy.backend.domain.booth.dto.boothParticipateDto.BoothParticipateRateResDto;
import com.synergy.backend.domain.booth.dto.boothParticipateDto.BoothParticipationInterestedResponseDto;
import com.synergy.backend.domain.booth.dto.BoothResponseDto;
import com.synergy.backend.domain.booth.entity.Booth;
import com.synergy.backend.domain.booth.entity.BoothParticipation;
import com.synergy.backend.domain.booth.exception.DuplicateParticipationException;
import com.synergy.backend.domain.booth.exception.NotFoundBoothException;
import com.synergy.backend.domain.booth.repository.BoothParticipationRepository;
import com.synergy.backend.domain.booth.repository.BoothRepository;
import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.conference.exception.NotFoundConference;
import com.synergy.backend.domain.conference.repository.ConferenceRepository;
import com.synergy.backend.domain.member.entity.Admin;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.exception.NotFoundUserException;
import com.synergy.backend.domain.member.repository.AdminRepository;
import com.synergy.backend.domain.member.repository.AttendeeRepository;
import com.synergy.backend.domain.point.service.PointService;
import com.synergy.backend.domain.qrCode.service.QrService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service @Slf4j
@RequiredArgsConstructor
public class BoothParticipationServiceImpl implements BoothParticipationService {

    private final BoothParticipationRepository boothParticipationRepository;
    private final BoothRepository boothRepository;
    private final ConferenceRepository conferenceRepository;
    private final AttendeeRepository attendeeRepository;
    private final AdminRepository adminRepository;
    private final PointService pointService;
    private final QrService qrService;

    @Transactional
    @Override
    public BoothResponseDto participateInBooth(String identifier, Long boothId, String secretCode) {
        Attendee attendee = attendeeRepository.findByEmail(identifier)
                .orElseThrow(NotFoundUserException::new);

        String decodingSecretCode = qrService.decodingSecretCode(secretCode);
        log.info("secretCode = {}", secretCode);
        log.info("decodingSecretCode = {}", decodingSecretCode);
        Booth booth = boothRepository.findByIdAndSecretCode(boothId, decodingSecretCode)
                .orElseThrow(NotFoundBoothException::new);

        boothParticipationRepository.existsByBoothIdAndAttendeeId(boothId, attendee.getId())
                        .ifPresent(boothParticipation -> {
                                throw new DuplicateParticipationException();
                        });

        boothParticipationRepository.save(BoothParticipation.of(booth, attendee));
        pointService.addBoothPoint(attendee.getId(), boothId);

        return BoothResponseDto.from(booth);
    }

    @Override
    public BoothParticipateRateResDto boothParticipateRate(String identifier, Long conferenceId) {
        Admin currentMember = findIfAdminExists(identifier);
        ifConferenceExists(conferenceId);
        findIfConferenceMine(currentMember, conferenceId);
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));

        return boothRepository.searchBoothRank(conferenceId, now);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BoothParticipationInterestedResponseDto> getParticipationCountByInterest(Long conferenceId) {
        return boothRepository.searchBoothParticipation(conferenceId);
    }

    private void findIfConferenceMine(Admin admin, Long conferenceId) {
        adminRepository.findByIdAndConferences_Id(admin.getId(), conferenceId);
    }

    private Conference ifConferenceExists(Long conferenceId) {
        return conferenceRepository.findById(conferenceId).orElseThrow(NotFoundConference::new);
    }

    private Admin findIfAdminExists(String identifier) {
        return adminRepository.findByAdminAuthCode(identifier).orElseThrow(NotFoundUserException::new);
    }
}
