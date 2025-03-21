package com.synergy.backend.domain.member.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.synergy.backend.domain.member.api.dto.resposne.RecruiterMyInfoResponseDto;
import com.synergy.backend.domain.member.entity.Recruiter;
import com.synergy.backend.domain.member.exception.NotFoundRecruiterException;
import com.synergy.backend.domain.member.repository.RecruiterRepository;

@ExtendWith(MockitoExtension.class)
class RecruiterServiceImplTest {

	@Mock
	private RecruiterRepository recruiterRepository;

	@InjectMocks
	private RecruiterServiceImpl recruiterService;

	private Recruiter recruiter;

	@BeforeEach
	void setUp() {
		recruiter = Recruiter.of("RC12345");
	}

	@DisplayName("채용담당자가 본인의 프로필을 조회한다.")
	@Test
	void testGetMyInformation_Success() {
		// given
		Long recruiterId = 1L;
		when(recruiterRepository.findById(recruiterId)).thenReturn(Optional.of(recruiter));

		// when
		RecruiterMyInfoResponseDto response = recruiterService.getMyInformation(recruiterId);

		// then
		assertEquals(RecruiterMyInfoResponseDto.from(recruiter), response);
	}

	@DisplayName("존재하지 않는 아이디의 채용담당자 조회 시도 시 예외가 발생한다.")
	@Test
	void testGetMyInformation_NotFound() {
		// given
		Long recruiterId = 1L;
		when(recruiterRepository.findById(recruiterId)).thenReturn(Optional.empty());

		//when & then
		assertThrows(NotFoundRecruiterException.class, () -> recruiterService.getMyInformation(recruiterId));
	}
}
