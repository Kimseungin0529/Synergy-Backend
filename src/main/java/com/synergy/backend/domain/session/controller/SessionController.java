package com.synergy.backend.domain.session.controller;

import com.synergy.backend.domain.session.dto.SessionDetailResDto;
import com.synergy.backend.domain.session.dto.SessionReqDto;
import com.synergy.backend.domain.session.dto.SessionResDto;
import com.synergy.backend.domain.session.dto.question.QuestionReqDto;
import com.synergy.backend.domain.session.dto.question.QuestionResDto;
import com.synergy.backend.domain.session.service.SessionService;
import com.synergy.backend.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/conference/{conferenceId}/session")
public class SessionController {

    private final SessionService sessionService;

    @PostMapping
    public ApiResponse createSession(@PathVariable(name = "conferenceId") Long conferenceId, @RequestBody SessionReqDto sessionReqDto) {
        sessionService.createSession(conferenceId, sessionReqDto);

        return ApiResponse.ok("Session created successfully!", 200);
    }

    @GetMapping
    public ApiResponse getSessions(@PathVariable(name = "conferenceId") Long conferenceId) {
        List<SessionResDto> result = sessionService.getSessions(conferenceId);

        return ApiResponse.ok(result, 200);
    }

    @GetMapping("/{sessionId}")
    public ApiResponse getSession(@PathVariable(name = "conferenceId") Long conferenceId, @PathVariable(name = "sessionId") Long sessionId) {
        SessionDetailResDto result = sessionService.getSessionInfo(conferenceId, sessionId);

        return ApiResponse.ok(result, 200);
    }

    @PatchMapping
    public ApiResponse updateSession(@RequestParam Long sessionId, @RequestBody SessionReqDto sessionReqDto) {
        sessionService.updateSession(sessionId, sessionReqDto);

        return ApiResponse.ok("Session updated successfully!", 200);
    }

    @DeleteMapping
    public ApiResponse deleteSession(@RequestParam Long sessionId) {
        sessionService.deleteSession(sessionId);

        return ApiResponse.ok("Session deleted successfully!", 200);
    }

    /* ------------------------------------------ Q&A --------------------------------------*/

    @PostMapping("/{sessionId}/participation")
    public ApiResponse createQuestion(@PathVariable(name = "conferenceId") Long conferenceId,
                                                      @PathVariable(name = "sessionId") Long sessionId,
                                                      @RequestBody QuestionReqDto reqDto) {

        sessionService.createQuestion(conferenceId, sessionId, reqDto);
        return ApiResponse.ok("Question created successfully!", 200);
    }

}
