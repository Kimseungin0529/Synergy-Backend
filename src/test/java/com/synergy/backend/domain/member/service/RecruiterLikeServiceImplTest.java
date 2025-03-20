package com.synergy.backend.domain.member.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.Recruiter;
import com.synergy.backend.domain.member.entity.RecruiterAttendeeLike;
import com.synergy.backend.domain.member.exception.DuplicateLikeException;
import com.synergy.backend.domain.member.repository.AttendeeRepository;
import com.synergy.backend.domain.member.repository.RecruiterAttendeeLikeRepository;
import com.synergy.backend.domain.member.repository.RecruiterRepository;

@ExtendWith(MockitoExtension.class)
class RecruiterLikeServiceImplTest {

	@Mock
	private RecruiterAttendeeLikeRepository recruiterAttendeeLikeRepository;

	@Mock
	private RecruiterRepository recruiterRepository;

	@Mock
	private AttendeeRepository attendeeRepository;

	@InjectMocks
	private RecruiterLikeServiceImpl recruiterLikeService;

	private Recruiter recruiter;
	private Attendee attendee;
	private RecruiterAttendeeLike like;

	@BeforeEach
	void setUp() {
		recruiter = Recruiter.of("RECRUITER12345");
		attendee = Attendee.of("user@email.com", "pass", "user1", "01012345678");
		like = RecruiterAttendeeLike.of(recruiter, attendee);
	}

	@DisplayName("채용담당자가 참가자 좋아요를 한다.")
	@Test
	void likeAttendee() {
		// given
		when(recruiterRepository.findById(anyLong())).thenReturn(Optional.of(recruiter));
		when(attendeeRepository.findById(anyLong())).thenReturn(Optional.of(attendee));
		when(recruiterAttendeeLikeRepository.existsByRecruiterAndAttendee(recruiter, attendee)).thenReturn(false);

		// when
		recruiterLikeService.likeAttendee(1L, 1L);

		// then
		verify(recruiterAttendeeLikeRepository, times(1)).save(any(RecruiterAttendeeLike.class));
	}

	@DisplayName("이미 좋아요한 참가자를 다시 좋아요하면 예외가 발생한다.")
	@Test
	void likeAttendee_DuplicateLike() {
		// given
		when(recruiterRepository.findById(anyLong())).thenReturn(Optional.of(recruiter));
		when(attendeeRepository.findById(anyLong())).thenReturn(Optional.of(attendee));
		when(recruiterAttendeeLikeRepository.existsByRecruiterAndAttendee(recruiter, attendee)).thenReturn(true);

		// when & then
		assertThatThrownBy(() -> recruiterLikeService.likeAttendee(1L, 1L))
			.isInstanceOf(DuplicateLikeException.class);
	}

	@DisplayName("채용담당자가 참가자 좋아요를 취소한다.")
	@Test
	void unlikeAttendee() {
		// given
		when(recruiterRepository.findById(anyLong())).thenReturn(Optional.of(recruiter));
		when(attendeeRepository.findById(anyLong())).thenReturn(Optional.of(attendee));

		// when
		recruiterLikeService.unlikeAttendee(1L, 1L);

		// then
		verify(recruiterAttendeeLikeRepository, times(1)).deleteByRecruiterAndAttendee(recruiter, attendee);
	}

}
