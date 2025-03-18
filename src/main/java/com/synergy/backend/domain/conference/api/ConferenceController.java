package com.synergy.backend.domain.conference.api;

import com.synergy.backend.domain.conference.dto.requset.ConferenceCreateRequest;
import com.synergy.backend.domain.conference.dto.requset.ConferenceUpdateRequest;
import com.synergy.backend.domain.conference.dto.response.ConferenceCreateResponse;
import com.synergy.backend.domain.conference.dto.response.ConferenceUpdateResponse;
import com.synergy.backend.domain.conference.service.ConferenceService;
import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.domain.member.exception.AccessDeniedException;
import com.synergy.backend.global.common.ApiResponse;
import com.synergy.backend.global.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/conference")
public class ConferenceController {

    private final ConferenceService conferenceService;

    @PostMapping
    public ApiResponse<ConferenceCreateResponse> registerConference(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                    @RequestBody @Valid ConferenceCreateRequest request){
        String identifier = userDetails.getIdentifier();
        RoleType role = userDetails.getRole();

        if (role != RoleType.ADMIN) {
            throw new AccessDeniedException();
        }
        return ApiResponse.ok(conferenceService.registerConference(identifier, request), 201);
    }

    @PatchMapping("/{id}")
    public ApiResponse<ConferenceUpdateResponse> updateConference(@AuthenticationPrincipal CustomUserDetails userDetails,
                                @PathVariable(name = "id") Long id, @RequestBody @Valid ConferenceUpdateRequest request){
        String identifier = userDetails.getIdentifier();
        RoleType role = userDetails.getRole();

        if (role != RoleType.ADMIN) {
            throw new AccessDeniedException();
        }
        return ApiResponse.ok(conferenceService.updateConference(identifier, id, request), 200);
    }
}
