package com.synergy.backend.module;

import com.synergy.backend.domain.member.api.AuthController;
import com.synergy.backend.domain.member.service.*;
import com.synergy.backend.global.mail.MailService;
import com.synergy.backend.global.token.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.synergy.backend.domain.booth.controller.BoothController;
import com.synergy.backend.domain.booth.service.BoothService;
import com.synergy.backend.domain.conference.api.ConferenceController;
import com.synergy.backend.domain.conference.service.ConferenceService;
import com.synergy.backend.domain.member.api.AdminController;
import com.synergy.backend.domain.member.api.AttendeeController;
import com.synergy.backend.domain.member.api.RecruiterController;
import com.synergy.backend.domain.point.api.PointController;
import com.synergy.backend.domain.point.service.PointService;
import com.synergy.backend.global.jwt.JwtProvider;
import com.synergy.backend.global.security.CustomUserDetailsService;

@WebMvcTest(controllers = {
	BoothController.class,
	ConferenceController.class,
	RecruiterController.class,
	AdminController.class,
	AttendeeController.class,
	PointController.class,
	AuthController.class
} /* 추가로 필요한 컨트롤러 클래스 지정 */)
public abstract class ControllerTestSupport {
	protected final ObjectMapper objectMapper = new ObjectMapper()
		.registerModule(new JavaTimeModule())
		.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	@Autowired
	protected MockMvc mockMvc;
	@MockitoBean
	protected BoothService boothService;
	@MockitoBean
	protected ConferenceService conferenceService;
	@MockitoBean
	protected RecruiterService recruiterService;
	@MockitoBean
	protected RecruiterAttendeeLikeService recruiterAttendeeLikeService;
	@MockitoBean
	protected JwtProvider jwtProvider;
	@MockitoBean
	protected CustomUserDetailsService userDetailsService;
	@MockitoBean
	protected AdminService adminService;
	@MockitoBean
	protected AttendeeService attendeeService;
	@MockitoBean
	protected PointService pointService;
	@MockitoBean
	protected AuthService authService;
	@MockitoBean
	protected MailService mailService;
	@MockitoBean
	protected CookieUtils cookieUtils;
}
