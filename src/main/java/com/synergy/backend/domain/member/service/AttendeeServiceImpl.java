package com.synergy.backend.domain.member.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.synergy.backend.domain.interest.entity.AttendeeInterest;
import com.synergy.backend.domain.interest.entity.Interest;
import com.synergy.backend.domain.interest.exception.NotFoundInterestException;
import com.synergy.backend.domain.interest.repository.AttendeeInterestRepository;
import com.synergy.backend.domain.interest.repository.InterestRepository;
import com.synergy.backend.domain.job.JobGroup;
import com.synergy.backend.domain.job.JobGroupRepository;
import com.synergy.backend.domain.job.JobPosition;
import com.synergy.backend.domain.job.JobPositionRepository;
import com.synergy.backend.domain.job.exception.NotFoundJobGroupException;
import com.synergy.backend.domain.job.exception.NotFoundJobPositionException;
import com.synergy.backend.domain.member.api.dto.request.JobInfoDetailsRequestDto;
import com.synergy.backend.domain.member.api.dto.request.JobInfoRequestDto;
import com.synergy.backend.domain.member.api.dto.resposne.AttendeeFullInfoResponseDto;
import com.synergy.backend.domain.member.api.dto.resposne.MyInfoResponseDto;
import com.synergy.backend.domain.member.api.dto.resposne.NextPointResponseDto;
import com.synergy.backend.domain.member.api.dto.resposne.ProfileImageUpdatedResponseDto;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.domain.member.entity.details.AgeGroup;
import com.synergy.backend.domain.member.entity.details.BaseAttendeeDetailEnum;
import com.synergy.backend.domain.member.entity.details.ConferenceParticipationPurpose;
import com.synergy.backend.domain.member.entity.details.EducationLevelType;
import com.synergy.backend.domain.member.entity.details.ExperienceLevelType;
import com.synergy.backend.domain.member.entity.details.MembershipLevelType;
import com.synergy.backend.domain.member.entity.details.PreferredCorporateCulture;
import com.synergy.backend.domain.member.entity.details.RegionType;
import com.synergy.backend.domain.member.entity.details.WorkplaceSelectionFactor;
import com.synergy.backend.domain.member.exception.AccessDeniedException;
import com.synergy.backend.domain.member.exception.NotFoundUserException;
import com.synergy.backend.domain.member.repository.AttendeeRepository;
import com.synergy.backend.domain.member.vo.NextPointInfo;
import com.synergy.backend.global.util.file.dto.FileInformationDto;
import com.synergy.backend.global.util.file.exception.EmptyImageFileException;
import com.synergy.backend.global.util.file.util.FileS3Util;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendeeServiceImpl implements AttendeeService {

	private final AttendeeRepository attendeeRepository;
	private final InterestRepository interestRepository;
	private final AttendeeInterestRepository attendeeInterestRepository;
	private final JobPositionRepository jobPositionRepository;
	private final JobGroupRepository jobGroupRepository;
	private final FileS3Util fileS3Util;

	/** 직무 정보 추가 */
	@Transactional
	@Override
	public void addJobInfo(Long id, JobInfoRequestDto request) {
		Attendee attendee = findAttendeeById(id);

		addInterests(attendee, request.interestCodes());

		attendee.updateJobInfo(
			findJobPositionByCode(request.jobPositionCode()),
			findJobGroupByCode(request.jobGroupCode()),
			request.hiringInterested()
		);
	}

	/** 직무 상세 정보 추가 */
	@Transactional
	@Override
	public void addJobInfoDetails(Long id, JobInfoDetailsRequestDto request, MultipartFile profileImage) {
		Attendee attendee = findAttendeeById(id);

		if (profileImage != null && !profileImage.isEmpty()) {
			attendee.addImage(fileS3Util.uploadFile(profileImage));
		}

		attendee.updateJobInfoDetails(
			findJobGroupByCode(request.desiredJobGroupCode()),
			findJobPositionByCode(request.desiredJobPositionCode()),
			convertToEnum(request.educationLevelCode(), EducationLevelType.class),
			convertToEnum(request.ageGroupCode(), AgeGroup.class),
			request.techStacks(),
			convertToEnum(request.experienceLevelCode(), ExperienceLevelType.class),
			convertToEnumSet(request.desiredWorkRegionCodes(), RegionType.class),
			request.selfIntroduction(),
			request.additionalInfo(),
			convertToEnumSet(request.workplaceSelectionFactorCodes(), WorkplaceSelectionFactor.class),
			convertToEnumSet(request.preferredCorporateCultureCodes(), PreferredCorporateCulture.class),
			convertToEnumSet(request.conferencePurposeCodes(), ConferenceParticipationPurpose.class)
		);
	}

	/** 내 정보 */
	@Transactional(readOnly = true)
	@Override
	public MyInfoResponseDto getMyInformation(Long id) {
		Attendee attendee = findAttendeeById(id);
		NextPointInfo info = MembershipLevelType.getNextLevelInfo(attendee.getTotalPoints());

		return MyInfoResponseDto.from(attendee, NextPointResponseDto.from(info));
	}

	/** 참가자 상세 정보 */
	@Transactional(readOnly = true)
	@Override
	public AttendeeFullInfoResponseDto getAttendeeInfoDetail(Long attendeeId, Long viewerId, RoleType role) {

		if (role == RoleType.ATTENDEE) {
			if (!viewerId.equals(attendeeId)) {
				throw new AccessDeniedException();
			}
		}

		Attendee attendee = findAttendeeById(attendeeId);

		return AttendeeFullInfoResponseDto.from(attendee);
	}

	@Transactional
	@Override
	public ProfileImageUpdatedResponseDto updateProfileImage(Long attendeeId,
		MultipartFile profileImage) {
		if (profileImage == null || profileImage.isEmpty()) {
			throw new EmptyImageFileException();
		}
		Attendee attendee = findAttendeeById(attendeeId);
		FileInformationDto fileInformationDto = fileS3Util.uploadFile(profileImage);
		attendee.addImage(fileInformationDto);
		return ProfileImageUpdatedResponseDto.from(fileInformationDto.accessUrl());
	}

	// 관심사 추가
	private void addInterests(Attendee attendee, Set<Integer> interestCodes) {
		if (interestCodes.size() > 3) {
			throw new IllegalArgumentException("관심사는 최대 3개까지 선택할 수 있습니다.");
		}

		Set<Interest> newInterests = getValidInterests(interestCodes);
		Set<Interest> currentInterests = getCurrentInterests(attendee);

		// 관심사 동일하면 변경하지 않음
		if (currentInterests.equals(newInterests)) {
			return;
		}

		// 변경이 필요한 경우에만 삭제 후 추가
		attendeeInterestRepository.deleteAll(attendee.getAttendeeInterests());
		attendee.getAttendeeInterests().clear();

		saveNewMemberInterests(attendee, newInterests);
	}

	// 관심사 코드 검증
	private Set<Interest> getValidInterests(Set<Integer> interestCodes) {
		List<Interest> interests = interestRepository.findAllByCodeIn(interestCodes);

		if (interests.size() != interestCodes.size()) {
			Set<Integer> foundCodes = interests.stream()
				.map(Interest::getCode)
				.collect(Collectors.toSet());

			Set<Integer> notFoundCodes = new HashSet<>(interestCodes);
			notFoundCodes.removeAll(foundCodes);

			throw new NotFoundInterestException("Not found interests: " + notFoundCodes);
		}

		return new HashSet<>(interests);
	}

	// 현재 등록된 관심사 가져오기
	private Set<Interest> getCurrentInterests(Attendee attendee) {
		return attendee.getAttendeeInterests()
			.stream()
			.map(AttendeeInterest::getInterest)
			.collect(Collectors.toSet());
	}

	// 새로운 관심사 저장
	private void saveNewMemberInterests(Attendee attendee, Set<Interest> newInterests) {
		Set<AttendeeInterest> newAttendeeInterests = newInterests.stream()
			.map(interest -> AttendeeInterest.of(attendee, interest))
			.collect(Collectors.toSet());

		attendeeInterestRepository.saveAll(newAttendeeInterests);
		attendee.getAttendeeInterests().addAll(newAttendeeInterests);
	}

	private JobGroup findJobGroupByCode(Integer jobGroupCode) {
		return jobGroupRepository.findByCode(jobGroupCode)
			.orElseThrow(NotFoundJobGroupException::new);
	}

	private JobPosition findJobPositionByCode(Integer jobPositionCode) {
		return jobPositionRepository.findByCode(jobPositionCode)
			.orElseThrow(NotFoundJobPositionException::new);
	}

	private Attendee findAttendeeById(Long attendeeId) {
		return attendeeRepository.findById(attendeeId)
			.orElseThrow(NotFoundUserException::new);
	}

	private <E extends Enum<E> & BaseAttendeeDetailEnum> E convertToEnum(Integer code, Class<E> enumClass) {
		return BaseAttendeeDetailEnum.fromCode(enumClass, code);
	}

	private <E extends Enum<E> & BaseAttendeeDetailEnum> Set<E> convertToEnumSet(Set<Integer> codes,
		Class<E> enumClass) {
		return codes.stream()
			.map(code -> BaseAttendeeDetailEnum.fromCode(enumClass, code))
			.collect(Collectors.toSet());
	}
}
