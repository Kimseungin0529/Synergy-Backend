package com.synergy.backend.domain.session.repository.sessionRepository;

import com.synergy.backend.domain.session.dto.sessionparticipateDto.SessionParticipateRateResDto;

import java.util.List;

public interface SessionCustomRepository {

    List<SessionParticipateRateResDto> getSessionParticipateByConferenceId(Long conferenceId);
}
