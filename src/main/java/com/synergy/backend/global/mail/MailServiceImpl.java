package com.synergy.backend.global.mail;

import java.time.Duration;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

	private static final long CODE_EXPIRE_MINUTES = 5;
	private static final long VERIFIED_EXPIRE_MINUTES = 10;
	private static final String REDIS_KEY_VERIFY_PREFIX = "email:verify:";
	private static final String REDIS_KEY_VERIFIED_PREFIX = "email:verified:";

	private final JavaMailSender mailSender;
	private final RedisTemplate<String, String> redisTemplate;

	@Value("${mail.smtp.address}")
	private String address;

	@Override
	public void sendVerificationCodeToMail(String email) throws MessagingException {
		String code = createVerificationCode();

		redisTemplate.opsForValue().set(getVerificationCodeKey(email), code, Duration.ofMinutes(CODE_EXPIRE_MINUTES));

		String body = String.format(
			"<h2>이메일 인증번호 안내</h2>"
				+ "<p>아래 인증번호를 <strong>%d분 이내</strong>에 입력해주세요.</p>"
				+ "<h3 style='color: #2d88ff;'>인증번호: <strong>%s</strong></h3>"
				+ "<p style='margin-top: 16px;'>"
				+ "⚠️ 인증 후 <strong>%d분 이내에 절차를 완료</strong>해야 인증이 유지됩니다.<br>"
				+ "인증이 만료되면 다시 요청해 주세요."
				+ "</p>"
				+ "<p style='margin-top: 24px;'>감사합니다.<br/>F'LINK 드림</p>"
			, CODE_EXPIRE_MINUTES, code, VERIFIED_EXPIRE_MINUTES
		);

		sendHtmlEmail(email, "이메일 인증", body);
	}

	@Override
	public void mailVerificationConfirm(String email, String code) {
		String storedCode = redisTemplate.opsForValue().get(getVerificationCodeKey(email));
		if (storedCode == null) {
			throw new VerificationCodeExpiredException();
		}
		if (!storedCode.equals(code)) {
			throw new VerificationCodeMismatchException();
		}

		// 인증 완료 마킹
		redisTemplate.opsForValue()
			.set(getVerifiedKey(email), "true", Duration.ofMinutes(VERIFIED_EXPIRE_MINUTES));

		// 인증 코드 삭제
		redisTemplate.delete(getVerificationCodeKey(email));
	}

	@Override
	public boolean isVerified(String email) {
		String verified = redisTemplate.opsForValue().get(getVerifiedKey(email));
		if (!"true".equals(verified)) {
			throw new EmailNotVerifiedException();
		}

		redisTemplate.delete(getVerifiedKey(email));
		return true;
	}

	private void sendHtmlEmail(String to, String subject, String htmlBody) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		message.setFrom(address);
		message.setRecipients(MimeMessage.RecipientType.TO, to);
		message.setSubject(subject);
		message.setText(htmlBody, "UTF-8", "html");
		mailSender.send(message);
	}

	private String getVerificationCodeKey(String email) {
		return REDIS_KEY_VERIFY_PREFIX + email;
	}

	private String getVerifiedKey(String email) {
		return REDIS_KEY_VERIFIED_PREFIX + email;
	}

	private String createVerificationCode() {
		String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder code = new StringBuilder();
		Random random = new Random();

		for (int i = 0; i < 6; i++) {
			int index = random.nextInt(characters.length());
			code.append(characters.charAt(index));
		}
		return code.toString();
	}
}
