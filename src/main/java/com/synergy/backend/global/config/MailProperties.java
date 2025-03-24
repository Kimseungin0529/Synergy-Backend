package com.synergy.backend.global.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mail.smtp")
public record MailProperties(
	String host,
	String address,
	String personal,
	String userName,
	String password,
	int port) {
}
