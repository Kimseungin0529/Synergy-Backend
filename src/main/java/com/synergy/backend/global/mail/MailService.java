package com.synergy.backend.global.mail;

public interface MailService {

	String sendVerificationCodeToMail(String email);

	void mailVerificationConfirm(String email, String code);

	boolean isVerified(String email);

	void clearVerification(String email);
}
