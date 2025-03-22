package com.synergy.backend.global.mail;

import jakarta.mail.MessagingException;

public interface MailService {
	void sendVerificationCodeToMail(String email) throws MessagingException;

	void mailVerificationConfirm(String email, String code);

	boolean isVerified(String email);
}
