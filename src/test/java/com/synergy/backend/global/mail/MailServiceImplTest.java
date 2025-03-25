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

import com.synergy.backend.global.mail.exception.EmailNotVerifiedException;
import com.synergy.backend.global.mail.exception.MailSendFailedException;
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

	@DisplayName("메일에 인증번호를 보내고 Redis에 저장 및 메일이 정상 발송된다.")
	@Test
	void sendVerificationCodeToMail_shouldStoreCodeAndSendMail() {
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

	@DisplayName("메일 전송 실패 시 MailSendFailedException을 던진다.")
	@Test
	void sendVerificationCodeToMail_shouldThrowMailSendFailedException_whenSendFails() {
		// given
		String email = "user@example.com";

		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
		doThrow(new RuntimeException("SMTP error")).when(mailSender).send(any(MimeMessage.class));

		// when & then
		assertThatThrownBy(() -> mailService.sendVerificationCodeToMail(email))
			.isInstanceOf(MailSendFailedException.class)
			.hasMessageContaining("SMTP error");
	}

	@DisplayName("올바른 인증번호 입력 시 인증 성공으로 Redis에 저장되고 기존 인증번호는 삭제된다.")
	@Test
	void mailVerificationConfirm_shouldSucceed_whenCodeMatches() {
		// given
		String email = "user@example.com";
		String key = REDIS_VERIFY_CODE_PREFIX + email;
		String code = "abc123";

		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		when(valueOperations.get(key)).thenReturn(code);

		// when
		mailService.mailVerificationConfirm(email, code);

		// then
		verify(valueOperations).set(REDIS_VERIFIED_PREFIX + email, "true",
			Duration.ofMinutes(VERIFICATION_SUCCESS_TTL_MINUTES));
		verify(redisTemplate).delete(key);
	}

	@DisplayName("인증번호가 없으면 VerificationCodeExpiredException을 던진다.")
	@Test
	void mailVerificationConfirm_shouldThrow_whenCodeMissing() {
		// given
		String email = "user@example.com";
		String key = REDIS_VERIFY_CODE_PREFIX + email;

		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		when(valueOperations.get(key)).thenReturn(null);

		// then
		assertThrows(VerificationCodeExpiredException.class, () ->
			mailService.mailVerificationConfirm(email, "abc123")
		);
	}

	@DisplayName("잘못된 인증번호 입력 시 VerificationCodeMismatchException을 던진다.")
	@Test
	void mailVerificationConfirm_shouldThrow_whenCodeMismatch() {
		// given
		String email = "user@example.com";
		String key = REDIS_VERIFY_CODE_PREFIX + email;

		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		when(valueOperations.get(key)).thenReturn("correctCode");

		// then
		assertThrows(VerificationCodeMismatchException.class, () ->
			mailService.mailVerificationConfirm(email, "wrongCode")
		);
	}

	@DisplayName("인증 여부 확인 시 true 반환 및 키 삭제")
	@Test
	void isVerified_shouldReturnTrue_whenVerified() {
		// given
		String email = "user@example.com";
		String key = REDIS_VERIFIED_PREFIX + email;

		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		when(valueOperations.get(key)).thenReturn("true");

		// when
		boolean result = mailService.isVerified(email);

		// then
		assertTrue(result);
		verify(redisTemplate).delete(key);
	}

	@DisplayName("인증 여부 확인 시 인증 정보 없으면 EmailNotVerifiedException 예외 발생")
	@Test
	void isVerified_shouldThrow_whenNotVerified() {
		// given
		String email = "user@example.com";
		String key = REDIS_VERIFIED_PREFIX + email;

		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		when(valueOperations.get(key)).thenReturn(null);

		// then
		assertThrows(EmailNotVerifiedException.class, () ->
			mailService.isVerified(email)
		);
	}
}
