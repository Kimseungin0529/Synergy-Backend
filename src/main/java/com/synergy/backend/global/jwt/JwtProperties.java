package com.synergy.backend.global.jwt;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
	String secret,
	Duration accessTokenExpiration,
	Duration refreshTokenExpiration
) {
}
