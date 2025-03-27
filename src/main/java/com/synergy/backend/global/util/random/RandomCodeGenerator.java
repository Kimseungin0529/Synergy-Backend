package com.synergy.backend.global.util.random;

import java.security.SecureRandom;

public class RandomCodeGenerator {

	private static final String NUMERIC = "0123456789";
	private static final String ALPHA_LOWER = "abcdefghijklmnopqrstuvwxyz";
	private static final String ALPHA_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String ALPHA = ALPHA_LOWER + ALPHA_UPPER;
	private static final String ALPHA_NUMERIC = ALPHA + NUMERIC;
	private static final String ALPHA_NUMERIC_SYMBOL = ALPHA_NUMERIC + "!@#$%^&*";

	private static final SecureRandom secureRandom = new SecureRandom();

	/**
	 * 1. 숫자만 (기본 6자리)
	 */
	public static String numericCode() {
		return generateFromCharset(NUMERIC, 6);
	}

	/**
	 * 2. 소문자 + 숫자 (기본 6자리)
	 */
	public static String lowercaseAlphaNumericCode() {
		return generateFromCharset(ALPHA_LOWER + NUMERIC, 6);
	}

	/**
	 * 3. 대/소문자 + 숫자 (기본 6자리)
	 */
	public static String alphaNumericCode() {
		return generateFromCharset(ALPHA_NUMERIC, 6);
	}

	/**
	 * 4. 특수문자 포함 (기본 8자리)
	 */
	public static String strongCodeWithSymbols() {
		return generateFromCharset(ALPHA_NUMERIC_SYMBOL, 8);
	}

	/**
	 * 5. 원하는 charset + 길이
	 */
	public static String generateFromCharset(String characters, int length) {
		StringBuilder code = new StringBuilder();
		for (int i = 0; i < length; i++) {
			code.append(characters.charAt(secureRandom.nextInt(characters.length())));
		}
		return code.toString();
	}
}
