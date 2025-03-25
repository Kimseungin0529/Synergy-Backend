package com.synergy.backend.global.token;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CookieUtils {
	public static final String REFRESH_TOKEN_NAME = "refreshToken";

	public String extractRefreshToken(HttpServletRequest request) {
		if (request.getCookies() == null)
			return null;

		return Arrays.stream(request.getCookies())
			.filter(cookie -> REFRESH_TOKEN_NAME.equals(cookie.getName()))
			.map(Cookie::getValue)
			.findFirst()
			.orElse(null);
	}

	public void addRefreshTokenToCookie(HttpServletResponse response, String refreshToken) {
		Cookie cookie = new Cookie(REFRESH_TOKEN_NAME, refreshToken);
		cookie.setHttpOnly(true); // JS 접근 불가
		cookie.setSecure(true); // HTTPS 환경에서만 전송
		cookie.setPath("/"); // 모든 경로에서 유효
		cookie.setMaxAge(30 * 24 * 60 * 60); // 30일

		// 필요 시 SameSite=Lax 혹은 Strict 설정 (Spring 6 이상이나 ResponseHeaderFilter 필요)
		response.addCookie(cookie);
	}
}
