package com.synergy.backend.domain.booth.service;

import com.google.zxing.WriterException;
import com.synergy.backend.domain.booth.dto.BoothRequestDto;
import com.synergy.backend.domain.booth.dto.BoothResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface BoothService {
    BoothResponseDto getBoothById(Long conferenceId, Long id);
    Page<BoothResponseDto> getAllBooths(Long conferenceId, Pageable pageable);
    BoothResponseDto createBooth(Long conferenceId, BoothRequestDto request, MultipartFile imageFile) throws WriterException;
    BoothResponseDto updateBooth(Long conferenceId, Long id, BoothRequestDto request, MultipartFile imageFile);
    void deleteBooth(Long conferenceId, Long id);
}
