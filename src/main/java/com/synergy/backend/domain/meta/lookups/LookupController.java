package com.synergy.backend.domain.meta.lookups;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.synergy.backend.domain.interest.repository.InterestRepository;
import com.synergy.backend.domain.job.JobGroupRepository;
import com.synergy.backend.domain.job.JobPositionRepository;
import com.synergy.backend.global.annotation.DisableSwaggerSecurity;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "frontend 전용 API")
@RestController
@RequestMapping("/api/v1/frontend/lookups")
@RequiredArgsConstructor
public class LookupController {

	private final InterestRepository interestRepository;
	private final JobGroupRepository jobGroupRepository;
	private final JobPositionRepository jobPositionRepository;

	@DisableSwaggerSecurity
	@GetMapping
	public List<LookupResponseDto> getLookupData(@RequestParam LookupType type) {
		return switch (type) {
			case INTERESTS -> interestRepository.findAll().stream()
				.map(interest -> new LookupResponseDto(interest.getId(), interest.getCode(), interest.getName()))
				.toList();
			case JOB_GROUPS -> jobGroupRepository.findAll().stream()
				.map(group -> new LookupResponseDto(group.getId(), group.getCode(), group.getName()))
				.toList();
			case JOB_POSITIONS -> jobPositionRepository.findAll().stream()
				.map(position -> new LookupResponseDto(position.getId(), position.getCode(), position.getName()))
				.toList();
		};
	}
}
