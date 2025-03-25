package com.synergy.backend.domain.member.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.synergy.backend.domain.member.api.dto.resposne.AttendeeSimpleResponseDto;
import com.synergy.backend.domain.member.api.dto.resposne.LikedRecruiterResponseDto;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.Recruiter;
import com.synergy.backend.domain.member.entity.RecruiterAttendeeLike;
import com.synergy.backend.domain.member.exception.DuplicateLikeException;
import com.synergy.backend.domain.member.repository.AttendeeRepository;
import com.synergy.backend.domain.member.repository.RecruiterAttendeeLikeRepository;
import com.synergy.backend.domain.member.repository.RecruiterRepository;

@ExtendWith(MockitoExtension.class)
class RecruiterAttendeeLikeServiceImplTest {

	@Mock
	private RecruiterAttendeeLikeRepository recruiterAttendeeLikeRepository;

	@Mock
	private RecruiterRepository recruiterRepository;

	@Mock
	private AttendeeRepository attendeeRepository;

	@InjectMocks
	private RecruiterAttendeeLikeServiceImpl recruiterAttendeeLikeService;

	private Recruiter recruiter1;
	private Recruiter recruiter2;
	private Attendee attendee1;
	private Attendee attendee2;
	private RecruiterAttendeeLike like1;
	private RecruiterAttendeeLike like2;

	@BeforeEach
	void setUp() {
		recruiter1 = Recruiter.of("RECRUITER12345");
		recruiter2 = Recruiter.of("RECRUITER67890");
		attendee1 = Attendee.of("user1@email.com", "pass", "user1", "01012345678");
		attendee2 = Attendee.of("user2@email.com", "pass", "user2", "01012345678");
		like1 = RecruiterAttendeeLike.of(recruiter1, attendee1);
		like2 = RecruiterAttendeeLike.of(recruiter2, attendee2);
	}

	@DisplayName("채용담당자가 참가자 좋아요를 한다.")
	@Test
	void likeAttendee() {
		// given
		when(recruiterRepository.findById(anyLong())).thenReturn(Optional.of(recruiter1));
		when(attendeeRepository.findById(anyLong())).thenReturn(Optional.of(attendee1));
		when(recruiterAttendeeLikeRepository.existsByRecruiterAndAttendee(recruiter1, attendee1)).thenReturn(false);

		// when
		recruiterAttendeeLikeService.likeAttendee(1L, 1L);

		// then
		verify(recruiterAttendeeLikeRepository, times(1)).save(any(RecruiterAttendeeLike.class));
	}

	@DisplayName("이미 좋아요한 참가자를 다시 좋아요하면 예외가 발생한다.")
	@Test
	void likeAttendee_DuplicateLike() {
		// given
		when(recruiterRepository.findById(anyLong())).thenReturn(Optional.of(recruiter1));
		when(attendeeRepository.findById(anyLong())).thenReturn(Optional.of(attendee1));
		when(recruiterAttendeeLikeRepository.existsByRecruiterAndAttendee(recruiter1, attendee1)).thenReturn(true);

		// when & then
		assertThatThrownBy(() -> recruiterAttendeeLikeService.likeAttendee(1L, 1L))
			.isInstanceOf(DuplicateLikeException.class);
	}

	@DisplayName("채용담당자가 참가자 좋아요를 취소한다.")
	@Test
	void unlikeAttendee() {
		// given
		when(recruiterRepository.findById(anyLong())).thenReturn(Optional.of(recruiter1));
		when(attendeeRepository.findById(anyLong())).thenReturn(Optional.of(attendee1));

		// when
		recruiterAttendeeLikeService.unlikeAttendee(1L, 1L);

		// then
		verify(recruiterAttendeeLikeRepository, times(1)).deleteByRecruiterAndAttendee(recruiter1, attendee1);
	}

	@DisplayName("채용 담당자가 좋아요한 참가자 목록을 조회한다.")
	@Test
	void getLikedAttendees() {
		// given
		Long recruiterId = 1L;
		when(recruiterAttendeeLikeRepository.findAllByRecruiterId(recruiterId))
			.thenReturn(List.of(like1));

		// when
		List<AttendeeSimpleResponseDto> response = recruiterAttendeeLikeService.getLikedAttendees(recruiterId);

		// then
		assertThat(response).hasSize(1);
		assertThat(response.get(0).getName()).isEqualTo(attendee1.getName());
	}

	@DisplayName("참가자가 좋아요한 채용 담당자 목록을 조회한다.")
	@Test
	void getLikedRecruiters() {
		// given
		Long attendeeId = 10L;
		when(recruiterAttendeeLikeRepository.findAllByAttendeeId(attendeeId))
			.thenReturn(List.of(like1, like2));

		// when
		List<LikedRecruiterResponseDto> response = recruiterAttendeeLikeService.getLikedRecruiters(attendeeId);

		// then
		assertThat(response).hasSize(2);
		assertThat(response.get(0).name()).isEqualTo(recruiter1.getName());
		assertThat(response.get(1).name()).isEqualTo(recruiter2.getName());
	}
}
