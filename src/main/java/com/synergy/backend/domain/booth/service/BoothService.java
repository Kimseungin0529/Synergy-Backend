package com.synergy.backend.domain.booth.service;

import com.synergy.backend.domain.booth.dto.BoothRequestDto;
import com.synergy.backend.domain.booth.dto.BoothResponseDto;
import com.synergy.backend.global.common.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoothService {
    ApiResponse<BoothResponseDto> getBoothById(Long conferenceId, Long id);
    ApiResponse<Page<BoothResponseDto>> getAllBooths(Long conferenceId, Pageable pageable);
    ApiResponse<BoothResponseDto> createBooth(Long conferenceId, BoothRequestDto request);
    ApiResponse<BoothResponseDto> updateBooth(Long conferenceId, Long id, BoothRequestDto request);
    ApiResponse<Void> deleteBooth(Long conferenceId, Long id);
}
