package com.synergy.backend.domain.session.service;

import com.google.zxing.WriterException;
import com.synergy.backend.domain.session.dto.sessionDto.SessionDetailResDto;
import com.synergy.backend.domain.session.dto.sessionDto.SessionReqDto;
import com.synergy.backend.domain.session.dto.sessionDto.SessionResDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SessionService {

    void createSession(Long conferenceId, SessionReqDto reqDto) throws WriterException;

    List<SessionResDto> getSessions(Long conferenceId);

    SessionDetailResDto getSessionInfo(Long conferenceId, Long sessionId);

    void updateSession(Long sessionId, SessionReqDto reqDto);

    void deleteSession(Long sessionId);


}
