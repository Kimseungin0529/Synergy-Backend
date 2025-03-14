package com.synergy.backend.domain.member.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.synergy.backend.domain.interest.entity.Interest;
import com.synergy.backend.domain.interest.entity.MemberInterest;
import com.synergy.backend.domain.interest.repository.InterestRepository;
import com.synergy.backend.domain.interest.repository.MemberInterestRepository;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.exception.NotFoundUserException;
import com.synergy.backend.domain.member.repository.AttendeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendeeServiceImpl implements AttendeeService {

	private final AttendeeRepository attendeeRepository;
	private final InterestRepository interestRepository;
	private final MemberInterestRepository memberInterestRepository;

	@Transactional
	@Override
	public Set<Interest> addInterests(String email, Set<Long> interestIds) {
		Attendee managedAttendee = findAttendeeByEmail(email);

		Set<Long> existingInterestIds = getExistingInterestIds(managedAttendee);
		Set<Interest> newInterests = getNewInterests(interestIds, existingInterestIds);

		if (newInterests.isEmpty()) {
			return managedAttendee.getMemberInterests()
				.stream()
				.map(MemberInterest::getInterest)
				.collect(Collectors.toSet());
		}

		saveNewMemberInterests(managedAttendee, newInterests);

		return newInterests;
	}

	private Attendee findAttendeeByEmail(String email) {
		return attendeeRepository.findByEmail(email)
			.orElseThrow(NotFoundUserException::new);
	}

	private Set<Long> getExistingInterestIds(Attendee attendee) {
		return attendee.getMemberInterests()
			.stream()
			.map(memberInterest -> memberInterest.getInterest().getId())
			.collect(Collectors.toSet());
	}

	private Set<Interest> getNewInterests(Set<Long> interestIds, Set<Long> existingInterestIds) {
		return interestRepository.findAllById(interestIds)
			.stream()
			.filter(interest -> !existingInterestIds.contains(interest.getId()))
			.collect(Collectors.toSet());
	}

	private void saveNewMemberInterests(Attendee attendee, Set<Interest> newInterests) {
		Set<MemberInterest> memberInterestList = newInterests.stream()
			.map(interest -> new MemberInterest(attendee, interest))
			.collect(Collectors.toSet());

		memberInterestRepository.saveAll(memberInterestList);
		attendee.getMemberInterests().addAll(memberInterestList);
	}
}
