package com.synergy.backend.domain.session.controller;

import java.util.List;

import com.google.zxing.WriterException;
import com.synergy.backend.domain.member.entity.User;
import com.synergy.backend.domain.session.service.SessionParticipateService;
import com.synergy.backend.global.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.synergy.backend.domain.session.dto.sessionDto.SessionDetailResDto;
import com.synergy.backend.domain.session.dto.sessionDto.SessionReqDto;
import com.synergy.backend.domain.session.dto.sessionDto.SessionResDto;
import com.synergy.backend.domain.session.dto.questionDto.QuestionReqDto;
import com.synergy.backend.domain.session.service.SessionService;
import com.synergy.backend.global.common.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/conference/{conferenceId}/session")
public class SessionController {

	private final SessionService sessionService;
	private final SessionParticipateService sessionParticipateService;

	@PostMapping
	public ApiResponse createSession(@AuthenticationPrincipal CustomUserDetails user,
			@PathVariable(name = "conferenceId") Long conferenceId,
		@RequestBody SessionReqDto sessionReqDto) throws WriterException {
		sessionService.createSession(user.getIdentifier(), conferenceId, sessionReqDto);

		return ApiResponse.ok("Session created successfully!", 200);
	}

	@GetMapping
	public ApiResponse getSessions(@AuthenticationPrincipal CustomUserDetails user,
								   @PathVariable(name = "conferenceId") Long conferenceId) {
		List<SessionResDto> result = sessionService.getSessions(user.getIdentifier(), conferenceId);

		return ApiResponse.ok(result, 200);
	}

	@GetMapping("/{sessionId}")
	public ApiResponse getSession(@AuthenticationPrincipal CustomUserDetails user,
								  @PathVariable(name = "conferenceId") Long conferenceId,
		@PathVariable(name = "sessionId") Long sessionId) {
		SessionDetailResDto result = sessionService.getSessionInfo(user.getIdentifier(), user.getRole(), conferenceId, sessionId);

		return ApiResponse.ok(result, 200);
	}

	@PatchMapping
	public ApiResponse updateSession(@AuthenticationPrincipal CustomUserDetails user,
									 @RequestParam Long sessionId, @RequestBody SessionReqDto sessionReqDto) {
		sessionService.updateSession(user.getIdentifier(), sessionId, sessionReqDto);

		return ApiResponse.ok("Session updated successfully!", 200);
	}

	@DeleteMapping
	public ApiResponse deleteSession(@AuthenticationPrincipal CustomUserDetails user,
									 @RequestParam Long sessionId) {
		sessionService.deleteSession(user.getIdentifier(), sessionId);

		return ApiResponse.ok("Session deleted successfully!", 200);
	}

    /* ------------------------------------------ Q&A --------------------------------------*/

    @PostMapping("/verify")
    public ApiResponse<SessionResDto> verifyQRCode(@AuthenticationPrincipal CustomUserDetails user,
												   @RequestParam(name = "qrCode") String qrCode){
		SessionResDto sessionResDto = sessionParticipateService.verifyQRCode(user.getIdentifier(), qrCode);

		return ApiResponse.ok(sessionResDto, 200);
    }

    @PostMapping("/{sessionId}/participation")
    public ApiResponse createQuestion(@AuthenticationPrincipal CustomUserDetails user,
									  @PathVariable(name = "conferenceId") Long conferenceId,
                                                      @PathVariable(name = "sessionId") Long sessionId,
                                                      @RequestBody QuestionReqDto reqDto) {

        sessionParticipateService.createQuestion(user.getIdentifier(), conferenceId, sessionId, reqDto);
        return ApiResponse.ok("Question created successfully!", 200);
    }

}
