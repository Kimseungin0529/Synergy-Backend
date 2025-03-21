package com.synergy.backend.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.synergy.backend.domain.member.api.dto.resposne.RecruiterMyInfoResponseDto;
import com.synergy.backend.domain.member.entity.Recruiter;
import com.synergy.backend.domain.member.exception.NotFoundRecruiterException;
import com.synergy.backend.domain.member.repository.RecruiterRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecruiterServiceImpl implements RecruiterService {

	private final RecruiterRepository recruiterRepository;

	@Transactional(readOnly = true)
	@Override
	public RecruiterMyInfoResponseDto getMyInformation(Long id) {
		Recruiter recruiter = findRecruiterById(id);
		return RecruiterMyInfoResponseDto.from(recruiter);
	}

	private Recruiter findRecruiterById(Long id) {
		return recruiterRepository.findById(id).orElseThrow(NotFoundRecruiterException::new);
	}
}
