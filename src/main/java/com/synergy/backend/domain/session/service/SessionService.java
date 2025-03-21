package com.synergy.backend.domain.session.service;

import com.google.zxing.WriterException;
import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.domain.session.dto.sessionDto.SessionDetailResDto;
import com.synergy.backend.domain.session.dto.sessionDto.SessionReqDto;
import com.synergy.backend.domain.session.dto.sessionDto.SessionResDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface SessionService {

    void createSession(String identifier, Long conferenceId, SessionReqDto reqDto, MultipartFile multipartFile) throws WriterException;

    List<SessionResDto> getSessions(String identifier, Long conferenceId);

    SessionDetailResDto getSessionInfo(String identifier, RoleType role, Long conferenceId, Long sessionId);

    void updateSession(String identifier, Long sessionId, SessionReqDto reqDto, MultipartFile multipartFile);

    void deleteSession(String identifier, Long sessionId);


}
