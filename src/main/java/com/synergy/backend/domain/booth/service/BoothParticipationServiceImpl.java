package com.synergy.backend.domain.booth.service;

import com.synergy.backend.domain.booth.dto.BoothParticipationResponseDto;
import com.synergy.backend.domain.booth.entity.Booth;
import com.synergy.backend.domain.booth.entity.BoothParticipation;
import com.synergy.backend.domain.booth.exception.DuplicateParticipationException;
import com.synergy.backend.domain.booth.exception.NotFoundBoothException;
import com.synergy.backend.domain.booth.repository.BoothParticipationRepository;
import com.synergy.backend.domain.booth.repository.BoothRepository;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.exception.NotFoundUserException;
import com.synergy.backend.domain.member.repository.AttendeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoothParticipationServiceImpl implements BoothParticipationService {

    private final BoothParticipationRepository boothParticipationRepository;
    private final BoothRepository boothRepository;
    private final AttendeeRepository attendeeRepository;

    @Transactional
    @Override
    public void participateInBooth(Long attendeeId, Long boothId) {
        Attendee attendee = attendeeRepository.findById(attendeeId)
                .orElseThrow(NotFoundUserException::new);

        Booth booth = boothRepository.findById(boothId)
                .orElseThrow(NotFoundBoothException::new);

        if (boothParticipationRepository.existsByBoothIdAndAttendeeId(boothId, attendeeId)) {
            throw new DuplicateParticipationException();
        }

        boothParticipationRepository.save(BoothParticipation.of(booth, attendee));
    }

    @Transactional(readOnly = true)
    @Override
    public List<BoothParticipationResponseDto> getParticipationCountByInterest(Long boothId) {
        return boothParticipationRepository.findParticipationCountByInterest(boothId);
    }
}
