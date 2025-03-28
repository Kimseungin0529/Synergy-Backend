package com.synergy.backend.domain.member.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.synergy.backend.domain.interest.entity.Interest;
import com.synergy.backend.domain.interest.repository.AttendeeInterestRepository;
import com.synergy.backend.domain.interest.repository.InterestRepository;
import com.synergy.backend.domain.job.JobGroup;
import com.synergy.backend.domain.job.JobGroupRepository;
import com.synergy.backend.domain.job.JobPosition;
import com.synergy.backend.domain.job.JobPositionRepository;
import com.synergy.backend.domain.member.api.dto.request.JobInfoDetailsRequestDto;
import com.synergy.backend.domain.member.api.dto.request.JobInfoRequestDto;
import com.synergy.backend.domain.member.api.dto.resposne.AttendeeFullInfoResponseDto;
import com.synergy.backend.domain.member.api.dto.resposne.MyInfoResponseDto;
import com.synergy.backend.domain.member.entity.Admin;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.domain.member.repository.AttendeeRepository;
import com.synergy.backend.global.security.CustomUserDetails;
import com.synergy.backend.global.util.file.dto.FileInformationDto;
import com.synergy.backend.global.util.file.util.FileS3Util;

@ExtendWith(MockitoExtension.class)
class AttendeeServiceImplTest {

	@Mock
	private AttendeeRepository attendeeRepository;
	@Mock
	private InterestRepository interestRepository;
	@Mock
	private AttendeeInterestRepository attendeeInterestRepository;
	@Mock
	private JobPositionRepository jobPositionRepository;
	@Mock
	private JobGroupRepository jobGroupRepository;
	@Mock
	private FileS3Util fileS3Util;

	@InjectMocks
	private AttendeeServiceImpl attendeeService;

	private Attendee attendee;
	private Attendee attendee2;
	private Admin admin;

	@BeforeEach
	void setUp() {
		attendee = Attendee.of("email@example.com", "password", "Tester", "01012345678");
		attendee2 = Attendee.of("email2@example.com", "password", "Tester2", "01012345678");
		admin = Admin.of("ADMIN1234");
	}

	@DisplayName("관리자가 참가자 정보를 조회한다.")
	@Test
	void getAttendeeInfoDetailByAdmin() {
		// given
		when(attendeeRepository.findById(1L)).thenReturn(Optional.ofNullable(attendee));

		// when
		AttendeeFullInfoResponseDto response = attendeeService.getAttendeeInfoDetail(1L, "ADMIN1234",
			RoleType.ADMIN);

		// then
		assertThat(response).isNotNull();
		assertEquals(attendee.getName(), response.baseInfo().name());
	}

	@DisplayName("참가자가 내 정보를 조회한다.")
	@Test
	void getMyInformation() {
		// given
		String identifier = "email@example.com";

		when(attendeeRepository.findByEmail(identifier)).thenReturn(Optional.of(attendee));

		// when
		MyInfoResponseDto response = attendeeService.getMyInformation(identifier);

		// then
		assertThat(response).isNotNull();
		assertThat(response.name()).isEqualTo(attendee.getName());
	}

	@DisplayName("직무 정보를 추가한다.")
	@Test
	void addJobInfo_success() {
		// given
		JobInfoRequestDto request = new JobInfoRequestDto(Set.of(1, 2), 100, 200, true);
		when(attendeeRepository.findByEmail(attendee.getEmail())).thenReturn(Optional.of(attendee));
		when(interestRepository.findAllByCodeIn(any())).thenReturn(List.of(mock(Interest.class), mock(Interest.class)));
		when(jobPositionRepository.findByCode(100)).thenReturn(Optional.of(mock(JobPosition.class)));
		when(jobPositionRepository.findByCode(200)).thenReturn(Optional.of(mock(JobPosition.class)));
		// when
		attendeeService.addJobInfo(attendee.getEmail(), request);

		// then
		verify(attendeeRepository).findByEmail(attendee.getEmail());
	}

	@DisplayName("직무 상세 정보를 추가한다.")
	@Test
	void addJobInfoDetails_success() {
		// given
		JobInfoDetailsRequestDto request = new JobInfoDetailsRequestDto(
			100, 200, 1, 2, "Java", 3, Set.of(11, 22), "자기소개", "추가정보",
			Set.of(1), Set.of(1), Set.of(1)
		);
		MultipartFile profileImage = mock(MultipartFile.class);
		when(profileImage.isEmpty()).thenReturn(false);
		when(attendeeRepository.findByEmail(attendee.getEmail())).thenReturn(Optional.of(attendee));
		when(jobGroupRepository.findByCode(anyInt())).thenReturn(Optional.of(mock(JobGroup.class)));
		when(jobPositionRepository.findByCode(anyInt())).thenReturn(Optional.of(mock(JobPosition.class)));

		// when
		attendeeService.addJobInfoDetails(attendee.getEmail(), request, profileImage);

		// then
		verify(attendeeRepository).findByEmail(attendee.getEmail());
	}

	@DisplayName("프로필 이미지를 업데이트한다.")
	@Test
	void updateProfileImage_success() {
		// given
		CustomUserDetails userDetails = new CustomUserDetails(attendee);
		MultipartFile profileImage = mock(MultipartFile.class);
		when(profileImage.isEmpty()).thenReturn(false);
		when(attendeeRepository.findById(attendee.getId())).thenReturn(Optional.of(attendee));

		FileInformationDto dto = new FileInformationDto("key", "url");
		when(fileS3Util.uploadFile(profileImage)).thenReturn(dto);

		// when
		var response = attendeeService.updateProfileImage(userDetails, profileImage);

		// then
		assertThat(response.profileImageUrl()).isEqualTo("url");

	}
}
