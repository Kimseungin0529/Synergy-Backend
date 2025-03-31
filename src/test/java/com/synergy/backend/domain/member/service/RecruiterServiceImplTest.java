package com.synergy.backend.domain.member.service;

import static com.synergy.backend.domain.member.entity.details.ExperienceLevelType.*;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import com.synergy.backend.domain.member.api.dto.request.AttendeeFilterRequest;
import com.synergy.backend.domain.member.api.dto.response.AttendeeListResponse;
import com.synergy.backend.domain.member.api.dto.response.AttendeeSimpleResponseDto;
import com.synergy.backend.domain.member.repository.AttendeeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.synergy.backend.domain.member.api.dto.response.RecruiterMyInfoResponseDto;
import com.synergy.backend.domain.member.entity.Recruiter;
import com.synergy.backend.domain.member.exception.NotFoundRecruiterException;
import com.synergy.backend.domain.member.repository.RecruiterRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class RecruiterServiceImplTest {

	@Mock
	private RecruiterRepository recruiterRepository;
	@Mock
	private AttendeeRepository attendeeRepository;

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

	@Test
	@DisplayName("필터 조건에 맞는 참가자 리스트를 페이징하여 반환한다.")
	void testGetAttendeesBy() {
		// given
		Long recruiterId = 1L;
		Pageable pageable = PageRequest.of(0, 5);
		AttendeeFilterRequest requestCondition = AttendeeFilterRequest.of(
			List.of("백엔드", "프론트엔드"),
			"4년제 졸업",
			"20~24세 이하",
			"1~2년 이하",
			List.of("서울", "부산")
		);
		List<AttendeeSimpleResponseDto> mockAttendees = List.of(
			new AttendeeSimpleResponseDto(1L, "김지원", "백엔드 개발자", JUNIOR, "Java, Spring", null, true),
			new AttendeeSimpleResponseDto(2L, "정서연", "프론트엔드 개발자", JUNIOR, "React", null, false)
		);

		Page<AttendeeSimpleResponseDto> mockPage = new PageImpl<>(mockAttendees, pageable, mockAttendees.size());
		given(attendeeRepository.searchPageAttendeesBy(eq(pageable), eq(recruiterId), eq(requestCondition)))
			.willReturn(mockPage);

		// when
		AttendeeListResponse result = recruiterService.getAttendeesBy(pageable, recruiterId, requestCondition);

		// then
		assertThat(result).isNotNull();
		assertThat(result)
			.extracting("currentPageNumber", "totalPages", "totalElements", "pageSize")
			.containsExactly(0, 1, 2L, 5);

		assertThat(result.getList())
			.hasSize(2)
			.extracting("name", "desiredJobPosition", "experienceLevel", "liked")
			.containsExactlyInAnyOrder(
				tuple("김지원", "백엔드 개발자", JUNIOR.getDescription(), true),
				tuple("정서연", "프론트엔드 개발자", JUNIOR.getDescription(), false)
			);
	}

}
