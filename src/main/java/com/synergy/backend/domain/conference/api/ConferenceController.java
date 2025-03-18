package com.synergy.backend.domain.conference.api;

import com.synergy.backend.domain.conference.dto.requset.ConferenceCreateRequest;
import com.synergy.backend.domain.conference.dto.requset.ConferenceUpdateRequest;
import com.synergy.backend.domain.conference.dto.response.ConferenceCreateResponse;
import com.synergy.backend.domain.conference.dto.response.ConferenceUpdateResponse;
import com.synergy.backend.domain.conference.service.ConferenceService;
import com.synergy.backend.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/conference")
public class ConferenceController {

    private final ConferenceService conferenceService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApiResponse<ConferenceCreateResponse> registerConference(@RequestBody @Valid ConferenceCreateRequest request){
        return ApiResponse.ok(conferenceService.registerConference(request), 201);
    }

    @PatchMapping("/{id}")
    public ApiResponse<ConferenceUpdateResponse> updateConference(@PathVariable(name = "id") Long id, @RequestBody @Valid ConferenceUpdateRequest request){
        return ApiResponse.ok(conferenceService.updateConference(id, request), 200);
    }
}
