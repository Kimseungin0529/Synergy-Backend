package com.synergy.backend.domain.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.synergy.backend.domain.member.api.dto.request.SignupAttendeeRequestDto;
import com.synergy.backend.domain.member.api.dto.resposne.SignupAttendeeResponseDto;
import com.synergy.backend.domain.member.api.dto.resposne.TokenResponseDto;
import com.synergy.backend.domain.member.vo.TokenWithRefreshToken;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.User;
import com.synergy.backend.domain.member.exception.DuplicateEmailException;
import com.synergy.backend.domain.member.exception.InvalidAccountInformationException;
import com.synergy.backend.domain.member.exception.InvalidAuthCodeException;
import com.synergy.backend.domain.member.exception.NotFoundUserException;
import com.synergy.backend.domain.member.exception.SameAsPreviousPasswordException;
import com.synergy.backend.domain.member.exception.UnauthorizedException;
import com.synergy.backend.domain.member.repository.AdminRepository;
import com.synergy.backend.domain.member.repository.AttendeeRepository;
import com.synergy.backend.domain.member.repository.RecruiterRepository;
import com.synergy.backend.domain.point.service.PointService;
import com.synergy.backend.global.jwt.JwtProvider;
import com.synergy.backend.global.mail.MailService;
import com.synergy.backend.global.mail.exception.EmailNotVerifiedException;
import com.synergy.backend.global.security.CustomUserDetails;
import com.synergy.backend.global.security.CustomUserDetailsService;
import com.synergy.backend.global.token.TokenService;
import com.synergy.backend.global.token.exception.InvalidRefreshTokenException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

	private final AttendeeRepository attendeeRepository;
	private final AdminRepository adminRepository;
	private final RecruiterRepository recruiterRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;
	private final PointService pointService;
	private final MailService mailService;
	private final TokenService tokenService;
	private final CustomUserDetailsService userDetailsService;

	@Transactional
	@Override
	public SignupAttendeeResponseDto registerAttendee(SignupAttendeeRequestDto request) {

		validateSignupRequest(request);

		Attendee attendee = Attendee.of(request.email(), encodePassword(request.password()), request.name(),
			request.phone());

		attendeeRepository.save(attendee);

		pointService.addSignupPoint(attendee.getId());

		return SignupAttendeeResponseDto.from(attendee);
	}

	@Transactional(readOnly = true)
	@Override
	public TokenWithRefreshToken loginAsAttendee(String email, String rawPassword) {
		Attendee attendee = findAttendeeByEmail(email);

		if (!isPasswordMatch(rawPassword, attendee.getPassword())) {
			throw new UnauthorizedException();
		}

		String accessToken = jwtProvider.generateAccessToken(new CustomUserDetails(attendee));
		String refreshToken = jwtProvider.generateRefreshToken(new CustomUserDetails(attendee));

		tokenService.storeRefreshToken(email, refreshToken);

		return TokenWithRefreshToken.of(refreshToken, TokenResponseDto.of(accessToken, attendee));
	}

	@Transactional(readOnly = true)
	@Override
	public TokenWithRefreshToken loginAsAdminOrRecruiter(String authCode) {
		User user = adminRepository.findByAdminAuthCode(authCode)
			.map(User.class::cast)
			.or(() -> recruiterRepository.findByRecruiterAuthCode(authCode).map(User.class::cast))
			.orElseThrow(InvalidAuthCodeException::new);

		String accessToken = jwtProvider.generateAccessToken(new CustomUserDetails(user));

		String refreshToken = jwtProvider.generateRefreshToken(new CustomUserDetails(user));
		tokenService.storeRefreshToken(authCode, refreshToken);

		return TokenWithRefreshToken.of(refreshToken, TokenResponseDto.of(accessToken, user));
	}

	@Transactional
	@Override
	public void passwordResetRequest(String email, String name, String phone) {
		validateEmailVerification(email);

		try {
			Attendee attendee = findAttendeeByEmail(email);
			if (!attendee.getName().equals(name) || !attendee.getPhone().equals(phone)) {
				throw new InvalidAccountInformationException();
			}
		} catch (NotFoundUserException e) {
			throw new InvalidAccountInformationException();
		}
	}

	@Transactional
	@Override
	public void passwordReset(String email, String newPassword) {
		Attendee attendee = findAttendeeByEmail(email);

		if (isPasswordMatch(newPassword, attendee.getPassword())) {
			throw new SameAsPreviousPasswordException();
		}

		attendee.updatePassword(encodePassword(newPassword));
	}

	@Override
	public TokenWithRefreshToken reissueRefreshToken(String currentRefreshToken) {

		if (!jwtProvider.validateToken(currentRefreshToken)) {
			throw new InvalidRefreshTokenException();
		}

		String identifier = jwtProvider.getIdentifierFromToken(currentRefreshToken);

		String savedRefreshToken = tokenService.getStoredRefreshToken(identifier);
		if (!currentRefreshToken.equals(savedRefreshToken)) {
			throw new InvalidRefreshTokenException();
		}

		CustomUserDetails userDetails = (CustomUserDetails)userDetailsService.loadUserByUsername(identifier);

		String newAccessToken = jwtProvider.generateAccessToken(userDetails);
		String newRefreshToken = jwtProvider.generateRefreshToken(userDetails);

		tokenService.storeRefreshToken(identifier, newRefreshToken);

		return TokenWithRefreshToken.of(newRefreshToken, TokenResponseDto.of(newAccessToken, userDetails.getUser()));
	}

	@Transactional(readOnly = true)
	private Attendee findAttendeeByEmail(String email) {
		return attendeeRepository.findByEmail(email).orElseThrow(NotFoundUserException::new);
	}

	private void validateSignupRequest(SignupAttendeeRequestDto request) {
		validateEmailVerification(request.email());
		validateEmailDuplicate(request.email());
	}

	private void validateEmailDuplicate(String email) {
		if (attendeeRepository.findByEmail(email).isPresent()) {
			throw new DuplicateEmailException();
		}
	}

	private void validateEmailVerification(String email) {
		if (!mailService.isVerified(email)) {
			throw new EmailNotVerifiedException();
		}
	}

	private boolean isPasswordMatch(String rawPassword, String encodedPassword) {
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}

	private String encodePassword(String rawPassword) {
		return passwordEncoder.encode(rawPassword);
	}
}
