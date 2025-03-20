package com.synergy.backend.domain.booth.service;

import com.synergy.backend.domain.booth.entity.Booth;
import com.synergy.backend.domain.booth.repository.BoothRepository;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.repository.AttendeeRepository;
import com.synergy.backend.domain.booth.entity.AttendeeBooth;
import com.synergy.backend.domain.booth.repository.AttendeeBoothRepository;
import com.synergy.backend.global.common.ApiResponse;
import com.synergy.backend.domain.booth.exception.NotFoundBoothException;
import com.synergy.backend.domain.member.exception.NotFoundUserException;
import com.synergy.backend.domain.booth.exception.DuplicateParticipationException;
import com.synergy.backend.domain.booth.exception.NotFoundParticipationException;
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
    public ApiResponse<String> participateInBooth(Long attendeeId, Long boothId) {
        Attendee attendee = attendeeRepository.findById(attendeeId)
                .orElseThrow(() -> new NotFoundUserException("참가자를 찾을 수 없습니다."));

        Booth booth = boothRepository.findById(boothId)
                .orElseThrow(() -> new NotFoundBoothException("부스를 찾을 수 없습니다."));

        if (attendeeBoothRepository.existsByBoothIdAndAttendeeId(boothId, attendeeId)) {
            throw new DuplicateParticipationException("이미 참여한 부스입니다.");
        }

        attendeeBoothRepository.save(new AttendeeBooth(booth, attendee));

        return ApiResponse.ok("부스 참여가 완료되었습니다.", 201);
    }

    @Transactional
    @Override
    public ApiResponse<String> cancelParticipation(Long attendeeId, Long boothId) {
        if (!attendeeBoothRepository.existsByBoothIdAndAttendeeId(boothId, attendeeId)) {
            throw new NotFoundParticipationException("참여 내역이 없습니다.");
        }

        attendeeBoothRepository.deleteByBoothIdAndAttendeeId(boothId, attendeeId);

        return ApiResponse.ok("부스 참여가 취소되었습니다.", 200);
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<String>> getParticipantsByBoothId(Long boothId) {
        Booth booth = boothRepository.findById(boothId)
                .orElseThrow(() -> new NotFoundBoothException("부스를 찾을 수 없습니다."));

        List<String> attendeeNames = attendeeBoothRepository.findByBooth(booth).stream()
                .map(attendeeBooth -> attendeeBooth.getAttendee().getName())
                .collect(Collectors.toList());

        return ApiResponse.ok(attendeeNames, 200);
    }
}
