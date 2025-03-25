package com.synergy.backend.global.token;

public interface TokenService {

	void storeRefreshToken(String identifier, String refreshToken);

	String getStoredRefreshToken(String identifier);

	void deleteRefreshToken(String identifier);

}
