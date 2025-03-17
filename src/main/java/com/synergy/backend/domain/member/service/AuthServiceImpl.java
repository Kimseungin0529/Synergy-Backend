package com.synergy.backend.domain.member.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.synergy.backend.domain.member.api.dto.request.SignupAttendeeRequestDto;
import com.synergy.backend.domain.member.api.dto.resposne.SignupAttendeeResponseDto;
import com.synergy.backend.domain.member.api.dto.resposne.TokenResponseDto;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.User;
import com.synergy.backend.domain.member.exception.DuplicateEmailException;
import com.synergy.backend.domain.member.exception.InvalidAuthCodeException;
import com.synergy.backend.domain.member.exception.NotFoundUserException;
import com.synergy.backend.domain.member.exception.UnauthorizedException;
import com.synergy.backend.domain.member.repository.AdminRepository;
import com.synergy.backend.domain.member.repository.AttendeeRepository;
import com.synergy.backend.domain.member.repository.RecruiterRepository;
import com.synergy.backend.domain.point.entity.PointType;
import com.synergy.backend.domain.point.service.PointService;
import com.synergy.backend.global.security.CustomUserDetails;
import com.synergy.backend.global.security.JwtProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

	private final AttendeeRepository attendeeRepository;
	private final AdminRepository adminRepository;
	private final RecruiterRepository recruiterRepository;
	private final PointService pointService;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;

	@Transactional
	@Override
	public SignupAttendeeResponseDto registerAttendee(SignupAttendeeRequestDto request) {

		if (attendeeRepository.findByEmail(request.email()).isPresent()) {
			throw new DuplicateEmailException();
		}

		Attendee attendee = Attendee.of(
			request.email(),
			encodePassword(request.password()),
			request.name(),
			request.phone());

		attendeeRepository.save(attendee);

		// 회원가입 시 포인트 적립
		pointService.addSignupPoint(attendee.getId());

		return SignupAttendeeResponseDto.from(attendee);
	}

	@Transactional
	@Override
	public TokenResponseDto loginAsAttendee(String email, String rawPassword) {
		Attendee attendee = findAttendeeWithEmail(email);

		if (!passwordEncoder.matches(rawPassword, attendee.getPassword())) {
			throw new UnauthorizedException();
		}

		String token = jwtProvider.generateToken(new CustomUserDetails(attendee));

		return new TokenResponseDto(token, attendee.getEmail(), attendee.getRole().toString());
	}

	@Transactional
	@Override
	public TokenResponseDto loginAsAdminOrRecruiter(String authCode) {
		User user = adminRepository.findByAdminAuthCode(authCode)
			.map(User.class::cast)
			.or(() -> recruiterRepository.findByRecruiterAuthCode(authCode).map(User.class::cast))
			.orElseThrow(InvalidAuthCodeException::new);

		CustomUserDetails userDetails = new CustomUserDetails(user);

		String token = jwtProvider.generateToken(userDetails);

		return new TokenResponseDto(token, authCode, user.getRole().toString());
	}

	@Transactional(readOnly = true)
	private Attendee findAttendeeWithEmail(String email) {
		return attendeeRepository.findByEmail(email).orElseThrow(NotFoundUserException::new);
	}

	@Transactional
	public void logout(String token) {
		if (token == null || token.isBlank()) {
			throw new IllegalArgumentException("Token must be provided for logout.");
		}
	}

	// 로그인된 유저객체 가져오기
	@Transactional(readOnly = true)
	public User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new NotFoundUserException();
		}

		Object principal = authentication.getPrincipal();
		if (!(principal instanceof CustomUserDetails userDetails)) {
			throw new NotFoundUserException();
		}

		String identifier = userDetails.getIdentifier();

		return attendeeRepository.findByEmail(identifier)
			.map(User.class::cast)
			.or(() -> adminRepository.findByAdminAuthCode(identifier).map(User.class::cast))
			.or(() -> recruiterRepository.findByRecruiterAuthCode(identifier).map(User.class::cast))
			.orElseThrow(NotFoundUserException::new);
	}

	private String encodePassword(String rawPassword) {
		return passwordEncoder.encode(rawPassword);
	}
}
