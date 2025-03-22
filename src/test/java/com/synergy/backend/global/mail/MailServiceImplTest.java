package com.synergy.backend.global.mail;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import com.synergy.backend.global.config.MailProperties;
import com.synergy.backend.global.mail.exception.EmailNotVerifiedException;
import com.synergy.backend.global.mail.exception.VerificationCodeExpiredException;
import com.synergy.backend.global.mail.exception.VerificationCodeMismatchException;

import jakarta.mail.internet.MimeMessage;

@ExtendWith(MockitoExtension.class)
class MailServiceImplTest {

	private static final long VERIFICATION_CODE_TTL_MINUTES = 5;
	private static final long VERIFICATION_SUCCESS_TTL_MINUTES = 10;
	private static final String REDIS_VERIFY_CODE_PREFIX = "email:verify:";
	private static final String REDIS_VERIFIED_PREFIX = "email:verified:";
	private final MailProperties mailProperties = new MailProperties(
		"smtp.test.com", "test@test.com", "TEST", "username", "password", 587
	);
	@Mock
	private JavaMailSender mailSender;
	@Mock
	private RedisTemplate<String, String> redisTemplate;
	@Mock
	private ValueOperations<String, String> valueOperations;
	@Mock
	private MimeMessage mimeMessage;
	@InjectMocks
	private MailServiceImpl mailService;

	@BeforeEach
	void setup() {
		ReflectionTestUtils.setField(mailService, "mailProperties", mailProperties);
	}

	@DisplayName("메일에 인증번호를 보내고 Redis에 정확한 key와 code를 저장한다.")
	@Test
	void sendVerificationCodeToMail_shouldStoreCodeInRedis() {
		// given
		String email = "user@example.com";

		ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> codeCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Duration> durationCaptor = ArgumentCaptor.forClass(Duration.class);

		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

		// when
		mailService.sendVerificationCodeToMail(email);

		// then
		verify(valueOperations).set(keyCaptor.capture(), codeCaptor.capture(), durationCaptor.capture());
		verify(mailSender).createMimeMessage();
		verify(mailSender).send(mimeMessage);

		assertThat(keyCaptor.getValue()).startsWith(REDIS_VERIFY_CODE_PREFIX + email);
		assertThat(codeCaptor.getValue()).matches("^[a-z0-9]{6}$");
		assertThat(durationCaptor.getValue()).isEqualTo(Duration.ofMinutes(VERIFICATION_CODE_TTL_MINUTES));
	}

	@DisplayName("올바른 인증번호 입력시 이메일 인증에 성공한다.")
	@Test
	void mailVerificationConfirm_shouldSucceed_whenCodeMatches() {
		String email = "user@example.com";
		String key = REDIS_VERIFY_CODE_PREFIX + email;
		String correctCode = "abc123";

		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		when(valueOperations.get(key)).thenReturn(correctCode);

		// when
		mailService.mailVerificationConfirm(email, correctCode);

		// then
		verify(valueOperations).set(REDIS_VERIFIED_PREFIX + email, "true",
			Duration.ofMinutes(VERIFICATION_SUCCESS_TTL_MINUTES));
		verify(redisTemplate).delete(key);
	}

	@DisplayName("인증번호 만료")
	@Test
	void mailVerificationConfirm_shouldFail_whenCodeMissing() {
		String email = "user@example.com";
		String key = REDIS_VERIFY_CODE_PREFIX + email;

		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		when(valueOperations.get(key)).thenReturn(null);

		assertThrows(VerificationCodeExpiredException.class, () ->
			mailService.mailVerificationConfirm(email, "abc123")
		);
	}

	@DisplayName("잘못된 코드 VerificationCodeMismatchException")
	@Test
	void mailVerificationConfirm_shouldFail_whenCodeMismatch() {
		String email = "user@example.com";
		String key = REDIS_VERIFY_CODE_PREFIX + email;

		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		when(valueOperations.get(key)).thenReturn("realcode");

		assertThrows(VerificationCodeMismatchException.class, () ->
			mailService.mailVerificationConfirm(email, "wrongcode")
		);
	}

	@Test
	void isVerified_shouldReturnTrue_whenVerified() {
		String email = "user@example.com";
		String key = REDIS_VERIFIED_PREFIX + email;

		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		when(valueOperations.get(key)).thenReturn("true");

		boolean result = mailService.isVerified(email);

		assertTrue(result);
		verify(redisTemplate).delete(key);
	}

	@Test
	void isVerified_shouldThrow_whenNotVerified() {
		String email = "user@example.com";
		String key = REDIS_VERIFIED_PREFIX + email;

		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		when(valueOperations.get(key)).thenReturn(null);

		assertThrows(EmailNotVerifiedException.class, () ->
			mailService.isVerified(email)
		);
	}

	// 메일이 성공적으로 보내진다.
	// 메일 전송 실패시 MailSendFailedException
	// redis에 설정한 ttl동안 이메일-인증번호가 저장된다.
	// redis에 저장된 인증번호가 만료되었거나 존재하지 않으면 VerificationCodeExpiredException
	// 올바른 인증번호 입력 시 이메일-인증번호는 삭제되고, reids에 설정한 ttl동안 이메일-성공이 저장된다.
	// redis에 저장된 인증번호가 입력한 인증번호와 일치하지 않으면 VerificationCodeMismatchException
	// 이메일 인증여부 확인시 true가 반환된다.
	// 이메일이 인증확인 상태가 아니면 (redis에 이메일-성공이 만료되었거나 존재하지 않으면) EmailNotVerifiedException
}
