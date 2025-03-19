package com.synergy.backend.domain.member.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.synergy.backend.domain.interest.repository.AttendeeInterestRepository;
import com.synergy.backend.domain.interest.repository.InterestRepository;
import com.synergy.backend.domain.job.JobCategoryRepository;
import com.synergy.backend.domain.job.OccupationCategoryRepository;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.repository.AttendeeRepository;

@ExtendWith(MockitoExtension.class)
class AttendeeServiceImplTest {

	@Mock
	private AttendeeRepository attendeeRepository;
	@Mock
	private InterestRepository interestRepository;
	@Mock
	private AttendeeInterestRepository attendeeInterestRepository;
	@Mock
	private JobCategoryRepository jobCategoryRepository;
	@Mock
	private OccupationCategoryRepository occupationCategoryRepository;

	@InjectMocks
	private AttendeeServiceImpl attendeeService;

	private Attendee attendee;

	@BeforeEach
	void setUp() {
		attendee = Attendee.of("email@example.com", "password", "Tester", "01012345678");
	}


}
