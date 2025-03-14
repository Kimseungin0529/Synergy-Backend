package com.synergy.backend.global.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.synergy.backend.domain.member.entity.Admin;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.Recruiter;
import com.synergy.backend.domain.member.exception.NotFoundUserException;
import com.synergy.backend.domain.member.repository.AdminRepository;
import com.synergy.backend.domain.member.repository.AttendeeRepository;
import com.synergy.backend.domain.member.repository.RecruiterRepository;
import com.synergy.backend.global.security.exception.UnKnownUserTypeException;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@Slf4j
class CustomUserDetailsServiceTest {

	@InjectMocks
	private CustomUserDetailsService userDetailsService;

	@Mock
	private AttendeeRepository attendeeRepository;

	@Mock
	private AdminRepository adminRepository;

	@Mock
	private RecruiterRepository recruiterRepository;

	@Test
	@DisplayName("Attendee 사용자의 identifier(email)로 UserDetails를 로드할 수 있다.")
	void loadUserByUsername_Attendee_Success() {
		// Given
		String email = "attendee@example.com";
		Attendee attendee = Attendee.of(email, "securePassword", "name", "phone");

		when(attendeeRepository.findByEmail(email)).thenReturn(Optional.of(attendee));

		// When
		CustomUserDetails userDetails = (CustomUserDetails)userDetailsService.loadUserByUsername(email);

		// Then
		assertNotNull(userDetails);
		assertEquals(email, userDetails.getIdentifier());
		verify(attendeeRepository).findByEmail(email);
	}

	@Test
	@DisplayName("Admin 사용자의 identifier(adminAuthCode)로 UserDetails를 로드할 수 있다.")
	void loadUserByUsername_Admin_Success() {
		// Given
		String adminAuthCode = "adminCode123";
		Admin admin = Admin.of(adminAuthCode);

		when(attendeeRepository.findByEmail(adminAuthCode)).thenReturn(Optional.empty());
		when(adminRepository.findByAdminAuthCode(adminAuthCode)).thenReturn(Optional.of(admin));

		// When
		CustomUserDetails userDetails = (CustomUserDetails)userDetailsService.loadUserByUsername(adminAuthCode);

		// Then
		assertNotNull(userDetails);
		assertEquals(adminAuthCode, userDetails.getIdentifier());
		verify(adminRepository).findByAdminAuthCode(adminAuthCode);
	}

	@Test
	@DisplayName("Recruiter 사용자의 identifier(recruiterAuthCode)로 UserDetails를 로드할 수 있다.")
	void loadUserByUsername_Recruiter_Success() {
		// Given
		String recruiterAuthCode = "recruiterCode456";
		Recruiter recruiter = Recruiter.of(recruiterAuthCode);

		when(attendeeRepository.findByEmail(recruiterAuthCode)).thenReturn(Optional.empty());
		when(adminRepository.findByAdminAuthCode(recruiterAuthCode)).thenReturn(Optional.empty());
		when(recruiterRepository.findByRecruiterAuthCode(recruiterAuthCode)).thenReturn(Optional.of(recruiter));

		// When
		CustomUserDetails userDetails = (CustomUserDetails)userDetailsService.loadUserByUsername(recruiterAuthCode);

		// Then
		assertNotNull(userDetails);
		assertEquals(recruiterAuthCode, userDetails.getIdentifier());
		verify(recruiterRepository).findByRecruiterAuthCode(recruiterAuthCode);
	}

	@Test
	@DisplayName("존재하지 않는 identifier로 UserDetails를 로드하면 예외가 발생한다.")
	void loadUserByUsername_UserNotFound_ThrowsException() {
		// Given
		String unknownIdentifier = "unknownUser";

		when(attendeeRepository.findByEmail(unknownIdentifier)).thenReturn(Optional.empty());
		when(adminRepository.findByAdminAuthCode(unknownIdentifier)).thenReturn(Optional.empty());
		when(recruiterRepository.findByRecruiterAuthCode(unknownIdentifier)).thenReturn(Optional.empty());

		// When & Then
		assertThrows(UnKnownUserTypeException.class, () -> userDetailsService.loadUserByUsername(unknownIdentifier));

		verify(attendeeRepository).findByEmail(unknownIdentifier);
		verify(adminRepository).findByAdminAuthCode(unknownIdentifier);
		verify(recruiterRepository).findByRecruiterAuthCode(unknownIdentifier);
	}
}
