package com.synergy.backend.global.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.domain.member.entity.User;
import com.synergy.backend.domain.member.repository.AdminRepository;
import com.synergy.backend.domain.member.repository.AttendeeRepository;
import com.synergy.backend.domain.member.repository.RecruiterRepository;
import com.synergy.backend.global.security.exception.UnKnownUserTypeException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final AdminRepository adminRepository;
	private final AttendeeRepository attendeeRepository;
	private final RecruiterRepository recruiterRepository;

	@Override
	public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
		return attendeeRepository.findByEmail(identifier)
			.map(this::createUserDetails)
			.or(() -> adminRepository.findByAdminAuthCode(identifier).map(this::createUserDetails))
			.or(() -> recruiterRepository.findByRecruiterAuthCode(identifier).map(this::createUserDetails))
			.orElseThrow(UnKnownUserTypeException::new);
	}

	public CustomUserDetails loadUserByUsernameAndRole(String identifier, RoleType roleType) {
		return switch (roleType) {
			case ATTENDEE -> attendeeRepository.findByEmail(identifier)
				.map(this::createUserDetails)
				.orElseThrow(() -> new UsernameNotFoundException("Attendee not found"));
			case ADMIN -> adminRepository.findByAdminAuthCode(identifier)
				.map(this::createUserDetails)
				.orElseThrow(() -> new UsernameNotFoundException("Admin not found"));
			case RECRUITER -> recruiterRepository.findByRecruiterAuthCode(identifier)
				.map(this::createUserDetails)
				.orElseThrow(() -> new UsernameNotFoundException("Recruiter not found"));
		};
	}

	public CustomUserDetails loadUserByUserIdAndRole(Long userId, RoleType roleType) {
		return switch (roleType) {
			case ATTENDEE -> attendeeRepository.findById(userId)
				.map(this::createUserDetails)
				.orElseThrow(() -> new UsernameNotFoundException("Attendee not found"));
			case ADMIN -> adminRepository.findById(userId)
				.map(this::createUserDetails)
				.orElseThrow(() -> new UsernameNotFoundException("Admin not found"));
			case RECRUITER -> recruiterRepository.findById(userId)
				.map(this::createUserDetails)
				.orElseThrow(() -> new UsernameNotFoundException("Recruiter not found"));
		};
	}

	private CustomUserDetails createUserDetails(User user) {
		return new CustomUserDetails(user);
	}
}
