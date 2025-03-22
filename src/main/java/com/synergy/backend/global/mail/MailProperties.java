package com.synergy.backend.global.mail;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@ConfigurationProperties(prefix = "mail.smtp")
@RequiredArgsConstructor
public class MailProperties {
	private final String host;
	private final String address;
	private final String personal;
	private final String userName;
	private final String password;
	private final int port;
}
