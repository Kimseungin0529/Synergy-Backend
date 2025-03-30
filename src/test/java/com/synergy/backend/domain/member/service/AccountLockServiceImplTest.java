package com.synergy.backend.domain.member.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import com.synergy.backend.domain.auth.AccountLockedEvent;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.User;
import com.synergy.backend.domain.member.repository.AttendeeRepository;
import com.synergy.backend.global.mail.MailService;

@ExtendWith(MockitoExtension.class)
class AccountLockServiceImplTest {

	@Mock
	private AttendeeRepository attendeeRepository;

	@Mock
	private ApplicationEventPublisher applicationEventPublisher;

	@InjectMocks
	private AccountLockServiceImpl accountLockService;

	@DisplayName("계정이 잠김상태로 바뀐다.")
	@Test
	void lockUserAccount_shouldLockAndSendEmail() {
		// given
		Long userId = 1L;
		String email = "test@example.com";
		User user = mock(User.class);
		when(user.getId()).thenReturn(userId);

		Attendee attendee = Attendee.of(email, "pass", "이름", "01012345678");

		when(attendeeRepository.findById(userId)).thenReturn(Optional.of(attendee));

		// when
		accountLockService.lockUserAccount(user);

		// then
		assertTrue(attendee.isLocked(), "계정이 잠겨야 함");
		verify(attendeeRepository).save(attendee);

		// ArgumentCaptor로 정확한 이벤트 객체 확인
		ArgumentCaptor<AccountLockedEvent> captor = ArgumentCaptor.forClass(AccountLockedEvent.class);
		verify(applicationEventPublisher).publishEvent(captor.capture());
		assertEquals(email, captor.getValue().email());
	}
}
