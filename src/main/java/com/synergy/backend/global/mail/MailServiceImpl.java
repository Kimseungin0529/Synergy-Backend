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

	private final JavaMailSender mailSender;
	private final RedisTemplate<String, String> redisTemplate;

	@Value("${mail.smtp.address}")
	private String address;

	@Override
	public void sendVerificationCodeToMail(String email) throws MessagingException {
		String code = createVerificationCode();

		String key = getRedisKey(email);
		redisTemplate.opsForValue().set(key, code, Duration.ofMinutes(CODE_EXPIRE_MINUTES));

		MimeMessage message = mailSender.createMimeMessage();
		message.setFrom(address);
		message.setRecipients(MimeMessage.RecipientType.TO, email);
		message.setSubject("이메일 인증");
		String body = "<h2>이메일 인증번호 안내</h2>"
			+ "<p>아래 인증번호를 <strong>" + CODE_EXPIRE_MINUTES + "분 이내</strong>에 입력해주세요.</p>"
			+ "<h3 style='color: #2d88ff;'>인증번호: <strong>" + code + "</strong></h3>"
			+ "<p style='margin-top: 16px;'>"
			+ "⚠️ 인증 후 <strong>" + VERIFIED_EXPIRE_MINUTES + "분 이내에 절차를 완료</strong>해야 인증이 유지됩니다.<br>"
			+ "인증이 만료되면 다시 요청해 주세요."
			+ "</p>"
			+ "<p style='margin-top: 24px;'>감사합니다.<br/>F'LINK 드림</p>";

		message.setText(body, "UTF-8", "html");
		mailSender.send(message);
	}

	@Override
	public void mailVerificationConfirm(String email, String code) {
		String storedCode = redisTemplate.opsForValue().get(getRedisKey(email));
		if (storedCode == null) {
			throw new IllegalStateException("인증번호가 존재하지 않거나 만료되었습니다.");
		}
		if (!storedCode.equals(code)) {
			throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
		}

		// 인증 완료 마킹
		redisTemplate.opsForValue().set("email:verified:" + email, "true", Duration.ofMinutes(VERIFIED_EXPIRE_MINUTES));
		redisTemplate.delete("email:verify:" + email);
	}

	@Override
	public boolean isVerified(String email) {
		String verified = redisTemplate.opsForValue().get("email:verified:" + email);
		if (!"true".equals(verified)) {
			throw new IllegalStateException("이메일 인증이 완료되지 않았습니다.");
		}

		redisTemplate.delete("email:verified:" + email);
		return true;
	}

	private String getRedisKey(String email) {
		return "email:verify:" + email;
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
