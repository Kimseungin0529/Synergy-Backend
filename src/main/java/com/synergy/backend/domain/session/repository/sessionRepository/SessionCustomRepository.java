package com.synergy.backend.domain.session.repository.sessionRepository;

import com.synergy.backend.domain.session.dto.sessionparticipateDto.SessionParticipateRateDetailResDto;
import com.synergy.backend.domain.session.dto.sessionparticipateDto.SessionParticipateRateResDto;

import java.time.LocalDate;
import java.util.List;

public interface SessionCustomRepository {

    List<SessionParticipateRateResDto> getSessionParticipateByConferenceId(Long conferenceId, LocalDate date);

    List<SessionParticipateRateDetailResDto> getSessionParticipateDetailByConferenceId(Long conferenceId);

    Long getSessionAttendeeCount(Long conferenceId);
}
