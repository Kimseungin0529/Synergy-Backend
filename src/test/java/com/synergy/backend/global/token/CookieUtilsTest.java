package com.synergy.backend.global.token;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletResponse;

class CookieUtilsTest {

	private final CookieUtils cookieUtils = new CookieUtils();

	@DisplayName("리프레시 토큰을 쿠키에 추가한다.")
	@Test
	void testAddRefreshTokenToCookie() {
		// given
		MockHttpServletResponse response = new MockHttpServletResponse();
		String token = "dummy-refresh-token";

		// when
		cookieUtils.addRefreshTokenToCookie(response, token);
		String setCookie = response.getHeader(HttpHeaders.SET_COOKIE);

		// then
		assertNotNull(setCookie);
		assertTrue(setCookie.contains("refreshToken=" + token));
		assertTrue(setCookie.contains("HttpOnly"));
		assertTrue(setCookie.contains("SameSite=None"));
		assertTrue(setCookie.contains("Path=/"));
	}

	@DisplayName("리프레시 토큰을 무효화한다.")
	@Test
	void testDeleteRefreshTokenCookie() {
		// given
		MockHttpServletResponse response = new MockHttpServletResponse();

		// when
		cookieUtils.deleteRefreshTokenCookie(response);

		String setCookie = response.getHeader(HttpHeaders.SET_COOKIE);

		// then
		assertNotNull(setCookie);
		assertTrue(setCookie.contains("refreshToken="));
		assertTrue(setCookie.contains("Max-Age=0"));
	}
}
