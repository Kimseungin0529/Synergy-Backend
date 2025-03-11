package com.synergy.backend.domain.booth.controller;

import com.synergy.backend.domain.booth.dto.BoothRequestDto;
import com.synergy.backend.domain.booth.dto.BoothResponseDto;
import com.synergy.backend.domain.booth.service.BoothService;
import com.synergy.backend.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/conference/{conferenceId}/booths")
@RequiredArgsConstructor
public class BoothController {

    private final BoothService boothService;

    @GetMapping("/{id}")
    public ApiResponse<BoothResponseDto> getBoothById(
            @PathVariable Long conferenceId,
            @PathVariable Long id
    ) {
        return boothService.getBoothById(conferenceId, id);
    }

    @GetMapping
    public ApiResponse<Page<BoothResponseDto>> getAllBooths(
            @PathVariable Long conferenceId,
            Pageable pageable
    ) {
        return boothService.getAllBooths(conferenceId, pageable);
    }

    @PostMapping
    public ApiResponse<BoothResponseDto> createBooth(
            @PathVariable Long conferenceId,
            @RequestBody BoothRequestDto request
    ) {
        return boothService.createBooth(conferenceId, request);
    }

    @PutMapping("/{id}")
    public ApiResponse<BoothResponseDto> updateBooth(
            @PathVariable Long conferenceId,
            @PathVariable Long id,
            @RequestBody BoothRequestDto request
    ) {
        return boothService.updateBooth(conferenceId, id, request);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteBooth(
            @PathVariable Long conferenceId,
            @PathVariable Long id
    ) {
        return boothService.deleteBooth(conferenceId, id);
    }
}