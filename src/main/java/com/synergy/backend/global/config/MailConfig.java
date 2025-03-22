package com.synergy.backend.global.config;

import java.util.Properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableConfigurationProperties(MailProperties.class)
@RequiredArgsConstructor
public class MailConfig {

	private final MailProperties mailProperties;

	@Bean
	public JavaMailSender javaMailService() {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setHost(mailProperties.host());
		javaMailSender.setUsername(mailProperties.userName());
		javaMailSender.setPassword(mailProperties.password());
		javaMailSender.setPort(mailProperties.port());
		javaMailSender.setJavaMailProperties(getMailProperties());
		javaMailSender.setDefaultEncoding("UTF-8");
		return javaMailSender;
	}

	private Properties getMailProperties() {
		Properties properties = new Properties();
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.starttls.enable", "true");
		properties.setProperty("mail.smtp.starttls.required", "true");
		properties.setProperty("mail.smtp.ssl.enable", "true");
		properties.setProperty("mail.smtp.ssl.trust", mailProperties.host());
		properties.setProperty("mail.debug", "true");
		return properties;
	}
}
