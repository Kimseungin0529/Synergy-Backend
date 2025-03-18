package com.synergy.backend.domain.conference.api;

import com.synergy.backend.domain.conference.dto.requset.ConferenceCreateRequest;
import com.synergy.backend.domain.conference.dto.requset.ConferenceUpdateRequest;
import com.synergy.backend.domain.conference.dto.response.ConferenceCreateResponse;
import com.synergy.backend.domain.conference.dto.response.ConferenceUpdateResponse;
import com.synergy.backend.domain.conference.service.ConferenceService;
import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.domain.member.exception.AccessDeniedException;
import com.synergy.backend.global.CurrentUser;
import com.synergy.backend.global.common.ApiResponse;
import com.synergy.backend.global.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor @Slf4j
@RequestMapping("/api/v1/conference")
public class ConferenceController {

    private final ConferenceService conferenceService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApiResponse<ConferenceCreateResponse> registerConference(@CurrentUser String identifier
                                                                , @RequestBody @Valid ConferenceCreateRequest request){
        return ApiResponse.ok(conferenceService.registerConference(identifier, request), 201);
    }

    @PatchMapping("/{id}")
    public ApiResponse<ConferenceUpdateResponse> updateConference(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                      @PathVariable(name = "id") Long id, @RequestBody @Valid ConferenceUpdateRequest request){
        if(RoleType.ADMIN.equals(userDetails.getRole())){
            throw new AccessDeniedException();
        }
        String identifier = userDetails.getIdentifier();
        return ApiResponse.ok(conferenceService.updateConference(identifier, id, request), 200);
    }
}
