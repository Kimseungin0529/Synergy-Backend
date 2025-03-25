package com.synergy.backend.global.mail;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.util.Random;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.synergy.backend.global.mail.exception.EmailNotVerifiedException;
import com.synergy.backend.global.mail.exception.MailSendFailedException;
import com.synergy.backend.global.mail.exception.VerificationCodeExpiredException;
import com.synergy.backend.global.mail.exception.VerificationCodeMismatchException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

	private static final long VERIFICATION_CODE_TTL_MINUTES = 5;
	private static final long VERIFICATION_SUCCESS_TTL_MINUTES = 10;
	private static final String REDIS_VERIFY_CODE_PREFIX = "email:verify:";
	private static final String REDIS_VERIFIED_PREFIX = "email:verified:";

	private final JavaMailSender mailSender;
	private final RedisTemplate<String, String> redisTemplate;
	private final MailProperties mailProperties;

	@Override
	public void sendVerificationCodeToMail(String email) {
		String code = generateVerificationCode();
		storeToRedis(getVerifyCodeKey(email), code, VERIFICATION_CODE_TTL_MINUTES);

		String body = buildEmailBody(code);
		sendHtmlEmail(email, "이메일 인증", body);
	}

	@Override
	public void mailVerificationConfirm(String email, String code) {
		String storedCode = getFromRedis(getVerifyCodeKey(email));

		if (storedCode == null) {
			throw new VerificationCodeExpiredException();
		}
		if (!storedCode.equals(code)) {
			throw new VerificationCodeMismatchException();
		}

		// 인증 완료 마킹
		storeToRedis(getVerifiedKey(email), "true", VERIFICATION_SUCCESS_TTL_MINUTES);

		// 인증 코드 삭제
		deleteFromRedis(getVerifyCodeKey(email));
	}

	@Override
	public boolean isVerified(String email) {
		String verified = getFromRedis(getVerifiedKey(email));
		if (!"true".equals(verified)) {
			throw new EmailNotVerifiedException();
		}

		deleteFromRedis(getVerifiedKey(email));
		return true;
	}

	// --- Redis Helpers ---
	private void storeToRedis(String key, String value, long minutes) {
		redisTemplate.opsForValue().set(key, value, Duration.ofMinutes(minutes));
	}

	private String getFromRedis(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	private void deleteFromRedis(String key) {
		redisTemplate.delete(key);
	}

	private String getVerifyCodeKey(String email) {
		return REDIS_VERIFY_CODE_PREFIX + email;
	}

	private String getVerifiedKey(String email) {
		return REDIS_VERIFIED_PREFIX + email;
	}

	// --- Mail helpers ---
	private void sendHtmlEmail(String to, String subject, String htmlBody) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			message.setFrom(new InternetAddress(mailProperties.address(), mailProperties.personal()));
			message.setRecipients(MimeMessage.RecipientType.TO, to);
			message.setSubject(subject);
			message.setText(htmlBody, "UTF-8", "html");
			mailSender.send(message);
		} catch (RuntimeException | MessagingException | UnsupportedEncodingException e) {
			throw new MailSendFailedException(e.getMessage());
		}
	}

	private String buildEmailBody(String verificationCode) {
		return String.format(
			"<h2>이메일 인증번호 안내</h2>"
				+ "<p>아래 인증번호를 <strong>%d분 이내</strong>에 입력해주세요.</p>"
				+ "<h3 style='color: #2d88ff;'>인증번호: <strong>%s</strong></h3>"
				+ "<p style='margin-top: 16px;'>"
				+ "⚠️ 인증 후 <strong>%d분 이내에 절차를 완료</strong>해야 인증이 유지됩니다.<br>"
				+ "인증이 만료되면 다시 요청해 주세요."
				+ "</p>"
				+ "<p style='margin-top: 24px;'>감사합니다.<br/>F'LINK 드림</p>"
			, VERIFICATION_CODE_TTL_MINUTES, verificationCode, VERIFICATION_SUCCESS_TTL_MINUTES
		);
	}

	private String generateVerificationCode() {
		String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder code = new StringBuilder();
		Random random = new Random();

		for (int i = 0; i < 6; i++) {
			code.append(characters.charAt(random.nextInt(characters.length())));
		}
		return code.toString();
	}
}
