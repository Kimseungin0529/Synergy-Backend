package com.synergy.backend.global.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.synergy.backend.domain.member.entity.Admin;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.Recruiter;
import com.synergy.backend.domain.member.repository.AdminRepository;
import com.synergy.backend.domain.member.repository.AttendeeRepository;
import com.synergy.backend.domain.member.repository.RecruiterRepository;
import com.synergy.backend.global.jwt.JwtProvider;
import com.synergy.backend.global.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtBatchGenerator {

	private final RecruiterRepository recruiterRepository;
	private final AdminRepository adminRepository;
	private final AttendeeRepository attendeeRepository;
	private final JwtProvider jwtProvider;

	public void generateTokensToCsv() throws IOException {
		List<String[]> rows = new ArrayList<>();
		rows.add(new String[] {"identifier", "role", "token"});

		Duration expiration = Duration.ofMinutes(30);

		// Recruiters
		for (Recruiter recruiter : recruiterRepository.findAll()) {
			CustomUserDetails details = new CustomUserDetails(recruiter);
			String token = jwtProvider.generateAccessToken(details);
			rows.add(new String[] {recruiter.getRecruiterAuthCode(), "RECRUITER", token});
		}

		// Admins
		for (Admin admin : adminRepository.findAll()) {
			CustomUserDetails details = new CustomUserDetails(admin);
			String token = jwtProvider.generateAccessToken(details);
			rows.add(new String[] {admin.getAdminAuthCode(), "ADMIN", token});
		}

		// Attendees
		for (Attendee attendee : attendeeRepository.findAll()) {
			CustomUserDetails details = new CustomUserDetails(attendee);
			String token = jwtProvider.generateAccessToken(details);
			rows.add(new String[] {attendee.getEmail(), "ATTENDEE", token});
		}

		// Save to CSV
		try (PrintWriter writer = new PrintWriter("jwt_tokens.csv")) {
			for (String[] row : rows) {
				writer.println(String.join(",", row));
			}
		}
	}
}
