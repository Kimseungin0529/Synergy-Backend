package com.synergy.backend.domain.booth.service;

import com.google.zxing.WriterException;
import com.synergy.backend.domain.booth.dto.BoothRequestDto;
import com.synergy.backend.domain.booth.dto.BoothDetailResponseDto;
import com.synergy.backend.domain.booth.dto.BoothResponseDto;
import com.synergy.backend.domain.member.entity.RoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface BoothService {
    BoothDetailResponseDto getBoothById(String identifier, RoleType role, Long conferenceId, Long id);
    Page<BoothResponseDto> getAllBooths(Long conferenceId, Pageable pageable);
    void createBooth(Long conferenceId, String router, BoothRequestDto request, MultipartFile imageFile) throws WriterException;
    BoothDetailResponseDto updateBooth(Long conferenceId, Long id, BoothRequestDto request, MultipartFile imageFile);
    void deleteBooth(Long conferenceId, Long id);
}