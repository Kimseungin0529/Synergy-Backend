package com.synergy.backend.domain.booth.service;

import com.synergy.backend.domain.booth.dto.BoothParticipationResponseDto;
import com.synergy.backend.domain.booth.dto.BoothResponseDto;
import com.synergy.backend.domain.booth.entity.Booth;
import com.synergy.backend.domain.booth.entity.BoothParticipation;
import com.synergy.backend.domain.booth.exception.DuplicateParticipationException;
import com.synergy.backend.domain.booth.exception.NotFoundBoothException;
import com.synergy.backend.domain.booth.repository.BoothParticipationRepository;
import com.synergy.backend.domain.booth.repository.BoothRepository;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.exception.NotFoundUserException;
import com.synergy.backend.domain.member.repository.AttendeeRepository;
import com.synergy.backend.domain.point.entity.PointType;
import com.synergy.backend.domain.point.service.PointService;
import com.synergy.backend.domain.qrCode.service.QrService;
import com.synergy.backend.domain.session.dto.sessionDto.SessionResDto;
import com.synergy.backend.domain.session.entity.AttendeeSession;
import com.synergy.backend.domain.session.entity.Session;
import com.synergy.backend.domain.session.exception.AlreadyAttendedException;
import com.synergy.backend.domain.session.exception.NotFoundSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @Slf4j
@RequiredArgsConstructor
public class BoothParticipationServiceImpl implements BoothParticipationService {

    private final BoothParticipationRepository boothParticipationRepository;
    private final BoothRepository boothRepository;
    private final AttendeeRepository attendeeRepository;
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

        if (boothParticipationRepository.existsByBoothIdAndAttendeeId(boothId, attendee.getId())) {
            throw new DuplicateParticipationException();
        }

        boothParticipationRepository.save(BoothParticipation.of(booth, attendee));
        pointService.addBoothPoint(attendee.getId(), boothId);

        return BoothResponseDto.from(booth);
    }
//        return SessionResDto.from(session);

    @Transactional(readOnly = true)
    @Override
    public List<BoothParticipationResponseDto> getParticipationCountByInterest(Long conferenceId) {
        return boothRepository.searchBoothParticipation(conferenceId);
    }
}
