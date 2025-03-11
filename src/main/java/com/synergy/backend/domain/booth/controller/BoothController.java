package com.synergy.backend.domain.booth.controller;

import com.synergy.backend.domain.booth.dto.BoothRequestDto;
import com.synergy.backend.domain.booth.dto.BoothResponseDto;
import com.synergy.backend.domain.booth.service.BoothService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/conference/{conferenceId}/booths")
@RequiredArgsConstructor
public class BoothController {

    private final BoothService boothService;

    @GetMapping("/{id}")
    public ResponseEntity<BoothResponseDto> getBoothById(
            @PathVariable Long conferenceId,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(boothService.getBoothById(conferenceId, id));
    }

    @GetMapping
    public ResponseEntity<Page<BoothResponseDto>> getAllBooths(
            @PathVariable Long conferenceId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(boothService.getAllBooths(conferenceId, pageable));
    }

    @PostMapping
    public ResponseEntity<BoothResponseDto> createBooth(
            @PathVariable Long conferenceId,
            @RequestBody BoothRequestDto request
    ) {
        return ResponseEntity.ok(boothService.createBooth(conferenceId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoothResponseDto> updateBooth(
            @PathVariable Long conferenceId,
            @PathVariable Long id,
            @RequestBody BoothRequestDto request
    ) {
        return ResponseEntity.ok(boothService.updateBooth(conferenceId, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooth(
            @PathVariable Long conferenceId,
            @PathVariable Long id
    ) {
        boothService.deleteBooth(conferenceId, id);
        return ResponseEntity.noContent().build();
    }
}
