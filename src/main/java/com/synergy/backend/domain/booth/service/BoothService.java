package com.synergy.backend.domain.booth.service;

import com.synergy.backend.domain.booth.dto.BoothRequestDto;
import com.synergy.backend.domain.booth.dto.BoothResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoothService {
    BoothResponseDto getBoothById(Long conferenceId, Long id);
    Page<BoothResponseDto> getAllBooths(Long conferenceId, Pageable pageable);
    BoothResponseDto createBooth(Long conferenceId, BoothRequestDto request);
    BoothResponseDto updateBooth(Long conferenceId, Long id, BoothRequestDto request);
    void deleteBooth(Long conferenceId, Long id);
}
