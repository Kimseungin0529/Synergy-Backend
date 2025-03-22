package com.synergy.backend.global.mail;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class MailServiceImplTest {
	@InjectMocks
	private MailServiceImpl mailService;

	@Mock
	private JavaMailSender mailSender;

	@Mock
	private RedisTemplate<String, String> redisTemplate;

	@Mock
	private ValueOperations<String, String> valueOperations;

	private String email;

	@BeforeEach
	void setUp() {
		ReflectionTestUtils.setField(mailService, "address", "noreply@example.com");
		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		email = "user@example.com";
	}

	@DisplayName("이메일 인증번호가 레디스에 저장된다.")
	@Test
	void storeMailVerificationCodeIntoRedis() {
		// given
		String keyPrefix = "email:verify:";
		String key = keyPrefix + email;

		// when
		// mailService.send(email);

		// then

	}


	// 이메일이 발송된다.
	// 인증번호가 검증된다.
	// 인증번호 완료시 검증완료상태가 레디스에 저장된다.
}
