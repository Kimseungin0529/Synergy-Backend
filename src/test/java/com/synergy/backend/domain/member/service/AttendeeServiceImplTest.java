package com.synergy.backend.domain.member.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
import org.springframework.test.util.ReflectionTestUtils;
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
import com.synergy.backend.domain.member.entity.Recruiter;
import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.domain.member.entity.details.AgeGroup;
import com.synergy.backend.domain.member.entity.details.EducationLevelType;
import com.synergy.backend.domain.member.entity.details.ExperienceLevelType;
import com.synergy.backend.domain.member.exception.AccessDeniedException;
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
	private Recruiter recruiter;

	@BeforeEach
	void setUp() {
		attendee = Attendee.of("email@example.com", "password", "Tester", "01012345678");
		ReflectionTestUtils.setField(attendee, "id", 1L);
		attendee2 = Attendee.of("email2@example.com", "password", "Tester2", "01012345678");
		ReflectionTestUtils.setField(attendee2, "id", 2L);
		admin = Admin.of("ADMIN1234");
		ReflectionTestUtils.setField(admin, "id", 1L);
		recruiter = Recruiter.of("RC1234");
		ReflectionTestUtils.setField(recruiter, "id", 1L);
	}

	@DisplayName("관리자가 참가자 상세 정보를 조회한다.")
	@Test
	void getAttendeeInfoDetailByAdmin() {
		// given
		when(attendeeRepository.findById(1L)).thenReturn(Optional.ofNullable(attendee));

		// when
		AttendeeFullInfoResponseDto response = attendeeService.getAttendeeInfoDetail(1L, 1L,
			RoleType.ADMIN);

		// then
		assertThat(response).isNotNull();
		assertEquals(attendee.getName(), response.baseInfo().name());
	}

	@DisplayName("채용담당자가 참가자 상세 정보를 조회한다.")
	@Test
	void getAttendeeInfoDetailByRecruiter() {
		// given
		when(attendeeRepository.findById(1L)).thenReturn(Optional.ofNullable(attendee));

		// when
		AttendeeFullInfoResponseDto response = attendeeService.getAttendeeInfoDetail(1L, 1L,
			RoleType.RECRUITER);

		// then
		assertThat(response).isNotNull();
		assertEquals(attendee.getName(), response.baseInfo().name());
	}

	@DisplayName("참가자가 본인의 상세 정보를 조회한다.")
	@Test
	void getAttendeeInfoDetailSelf() {
		// given
		when(attendeeRepository.findById(1L)).thenReturn(Optional.ofNullable(attendee));

		// when
		AttendeeFullInfoResponseDto response = attendeeService.getAttendeeInfoDetail(1L, 1L,
			RoleType.ATTENDEE);

		// then
		assertThat(response).isNotNull();
		assertEquals(attendee.getName(), response.baseInfo().name());
	}

	@DisplayName("다른 참가자가 상세 정보를 요청하면 예외를 반환한다.")
	@Test
	void getAttendeeInfoDetail_AccessDenied_ThrowsException() {
		// given
		Long attendeeId = 1L;
		Long viewerId = 2L;
		RoleType role = RoleType.ATTENDEE;

		// when & then
		assertThrows(AccessDeniedException.class, () -> {
			attendeeService.getAttendeeInfoDetail(attendeeId, viewerId, role);
		});
	}

	@DisplayName("참가자가 내 정보를 조회한다.")
	@Test
	void getMyInformation() {
		// given
		when(attendeeRepository.findById(1L)).thenReturn(Optional.ofNullable(attendee));

		// when
		MyInfoResponseDto response = attendeeService.getMyInformation(1L);

		// then
		assertThat(response).isNotNull();
		assertThat(response.name()).isEqualTo(attendee.getName());
	}

	@DisplayName("직무 정보를 추가한다.")
	@Test
	void addJobInfo_success() {
		// given
		JobInfoRequestDto request = new JobInfoRequestDto(Set.of(1, 2), 100, 200, true);
		when(attendeeRepository.findById(1L)).thenReturn(Optional.ofNullable(attendee));
		when(interestRepository.findAllByCodeIn(any())).thenReturn(List.of(mock(Interest.class), mock(Interest.class)));
		when(jobGroupRepository.findByCode(100)).thenReturn(Optional.of(mock(JobGroup.class)));
		when(jobPositionRepository.findByCode(200)).thenReturn(Optional.of(mock(JobPosition.class)));

		// when
		attendeeService.addJobInfo(attendee.getId(), request);

		// then
		assertThat(attendee.getAttendeeInterests()).isNotEmpty();
		assertThat(attendee.getCurrentJobGroup()).isNotNull();
		assertThat(attendee.getCurrentJobPosition()).isNotNull();
	}

	@DisplayName("직무 상세 정보를 추가한다.")
	@Test
	void addJobInfoDetails_success() {
		// given
		JobInfoDetailsRequestDto request = new JobInfoDetailsRequestDto(
			100, 200, 2, 20, "Java", 3, Set.of(11, 22), "자기소개", "추가정보",
			Set.of(1), Set.of(1), Set.of(1)
		);
		MultipartFile profileImage = mock(MultipartFile.class);
		when(profileImage.isEmpty()).thenReturn(false);
		when(fileS3Util.uploadFile(profileImage)).thenReturn(new FileInformationDto("key", "url"));
		when(attendeeRepository.findById(1L)).thenReturn(Optional.ofNullable(attendee));
		when(jobGroupRepository.findByCode(anyInt())).thenReturn(Optional.of(mock(JobGroup.class)));
		when(jobPositionRepository.findByCode(anyInt())).thenReturn(Optional.of(mock(JobPosition.class)));

		// when
		attendeeService.addJobInfoDetails(1L, request, profileImage);

		// then
		assertThat(attendee.getDesiredJobGroup()).isNotNull();
		assertThat(attendee.getDesiredJobPosition()).isNotNull();
		assertThat(attendee.getEducationLevel()).isEqualTo(EducationLevelType.fromCode(2));
		assertThat(attendee.getAgeGroup()).isEqualTo(AgeGroup.fromCode(20));
		assertThat(attendee.getTechStacks()).isEqualTo("Java");
		assertThat(attendee.getExperienceLevel()).isEqualTo(ExperienceLevelType.fromCode(3));
		assertThat(attendee.getDesiredWorkRegion()).hasSize(2);
		assertThat(attendee.getSelfIntroduction()).isEqualTo("자기소개");
	}

	@DisplayName("관심사를 3개 초과 입력하면 예외가 발생한다.")
	@Test
	void addJobInfo_interestLimitExceeded_throwsException() {
		// given
		JobInfoRequestDto request = new JobInfoRequestDto(Set.of(1, 2, 3, 4), 100, 200, true);
		when(attendeeRepository.findById(1L)).thenReturn(Optional.ofNullable(attendee));

		// when & then
		assertThrows(IllegalArgumentException.class, () -> attendeeService.addJobInfo(1L, request));
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
		var response = attendeeService.updateProfileImage(attendee.getId(), profileImage);

		// then
		assertThat(response.profileImageUrl()).isEqualTo("url");

	}

}
