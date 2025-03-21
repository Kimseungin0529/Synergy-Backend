package com.synergy.backend.domain.booth.service;

import java.util.List;

public interface BoothParticipationService {
    void participateInBooth(Long attendeeId, Long boothId);
    void cancelParticipation(Long attendeeId, Long boothId);
}
