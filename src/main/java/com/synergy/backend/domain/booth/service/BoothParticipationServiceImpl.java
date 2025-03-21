package com.synergy.backend.domain.booth.service;

import com.synergy.backend.domain.booth.entity.AttendeeBooth;
import com.synergy.backend.domain.booth.entity.Booth;
import com.synergy.backend.domain.booth.exception.DuplicateParticipationException;
import com.synergy.backend.domain.booth.exception.NotFoundBoothException;
import com.synergy.backend.domain.booth.exception.NotFoundParticipationException;
import com.synergy.backend.domain.booth.repository.AttendeeBoothRepository;
import com.synergy.backend.domain.booth.repository.BoothRepository;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.exception.NotFoundUserException;
import com.synergy.backend.domain.member.repository.AttendeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoothParticipationServiceImpl implements BoothParticipationService {

    private final AttendeeBoothRepository attendeeBoothRepository;
    private final BoothRepository boothRepository;
    private final AttendeeRepository attendeeRepository;

    @Transactional
    @Override
    public void participateInBooth(Long attendeeId, Long boothId) {
        Attendee attendee = attendeeRepository.findById(attendeeId)
                .orElseThrow(NotFoundUserException::new);

        Booth booth = boothRepository.findById(boothId)
                .orElseThrow(NotFoundBoothException::new);

        if (attendeeBoothRepository.existsByBoothIdAndAttendeeId(boothId, attendeeId)) {
            throw new DuplicateParticipationException("이미 참여한 부스입니다.");
        }

        attendeeBoothRepository.save(new AttendeeBooth(booth, attendee));
    }

    @Transactional
    @Override
    public void cancelParticipation(Long attendeeId, Long boothId) {
        if (!attendeeBoothRepository.existsByBoothIdAndAttendeeId(boothId, attendeeId)) {
            throw new NotFoundParticipationException("참여 내역이 없습니다.");
        }

        attendeeBoothRepository.deleteByBoothIdAndAttendeeId(boothId, attendeeId);
    }
}
