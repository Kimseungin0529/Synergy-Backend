package com.synergy.backend.global.mail;

public interface MailService {

	void sendVerificationCodeToMail(String email);

	void mailVerificationConfirm(String email, String code);

	boolean isVerified(String email);
}
