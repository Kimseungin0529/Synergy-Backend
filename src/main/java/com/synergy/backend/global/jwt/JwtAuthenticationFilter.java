package com.synergy.backend.global.jwt;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.global.common.ApiResponse;
import com.synergy.backend.global.security.CustomUserDetailsService;
import com.synergy.backend.global.security.exception.UnKnownUserTypeException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;
	private final CustomUserDetailsService userDetailsService;
	private final ObjectMapper objectMapper;

	private final List<String> excludedPathPrefixes = List.of("/api/v1/auth");

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String path = request.getRequestURI();
		return excludedPathPrefixes.stream().anyMatch(path::startsWith);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws IOException {

		try {
			String token = resolveToken(request);

			if (token != null && jwtProvider.validateToken(token)) {
				String username = jwtProvider.getIdentifierFromToken(token);
				RoleType role = jwtProvider.getRoleTypeFromToken(token);

				log.info("REQUEST identifier: {}, role: {}", username, role);

				UserDetails userDetails = userDetailsService.loadUserByUsername(username);

				UsernamePasswordAuthenticationToken authentication =
					new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}

			filterChain.doFilter(request, response);
		} catch (UnKnownUserTypeException e) {
			log.warn("Unknown user type", e);
			handleException(response, HttpServletResponse.SC_FORBIDDEN, "존재하지 않는 사용자입니다.");
		} catch (io.jsonwebtoken.ExpiredJwtException e) {
			log.warn("Expired JWT token", e);
			handleException(response, HttpServletResponse.SC_UNAUTHORIZED, "토큰이 만료되었습니다.");
		} catch (io.jsonwebtoken.JwtException e) {
			log.warn("Invalid JWT token", e);
			handleException(response, HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.");
		} catch (Exception e) {
			log.error("Authentication failed", e);
			handleException(response, HttpServletResponse.SC_UNAUTHORIZED, "인증 실패");
		}
	}

	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

	private void handleException(HttpServletResponse response, int status, String message) throws IOException {
		response.setStatus(status);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(
			objectMapper.writeValueAsString(
				ApiResponse.error(message, status)
			)
		);
		response.flushBuffer();
	}
}
