package com.synergy.backend.domain.auth;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.synergy.backend.global.mail.MailService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountLockedEventHandler {

	private final MailService mailService;

	@EventListener
	public void handleAccountLocked(AccountLockedEvent event) {
		mailService.sendVerificationCodeToMail(event.email());
	}
}
