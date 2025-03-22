package com.synergy.backend.global.mail;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

	private final JavaMailSender mailSender;
	private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();

	@Value("${mail.smtp.address}")
	private String address;

	@Override
	public void sendVerificationCodeToMail(String email) throws MessagingException {
		String code = createVerificationCode();
		verificationCodes.put(email, code);

		MimeMessage message = mailSender.createMimeMessage();
		message.setFrom(address);
		message.setRecipients(MimeMessage.RecipientType.TO, email);

		message.setSubject("이메일 인증");
		String body = "";
		body += "<h3>요청하신 인증 번호입니다.</h3>";
		body += "<p><strong>" + code + "</strong></p>";
		body += "<p>인증번호는 5분간 유효합니다.</p>";
		message.setText(body, "UTF-8", "html");

		mailSender.send(message);
	}

	@Override
	public void mailVerificationConfirm(String email, String code) {
		String storedCode = verificationCodes.get(email);
		if (storedCode == null) {
			throw new IllegalStateException("인증번호가 존재하지 않거나 만료되었습니다.");
		}
		if (!storedCode.equals(code)) {
			throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
		}

		// 성공 처리
		verificationCodes.remove(email); // 1회성 사용 후 제거
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
