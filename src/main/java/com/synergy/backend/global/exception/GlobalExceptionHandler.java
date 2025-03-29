package com.synergy.backend.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.synergy.backend.global.common.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final String LOG_FORMAT = "Class : {}, Code : {}, Message : {}";
	private static final int ERROR_CODE = 400;
	private static final int SERVER_ERROR_CODE = 500;

	@ExceptionHandler(BaseErrorException.class)
	public ResponseEntity<ExceptionResponse<Void>> handle(BaseErrorException e) {
		logWarning(e, e.getErrorCode());
		ExceptionResponse<Void> response = ExceptionResponse.fail(e.getErrorCode(), e.getMessage());

		return ResponseEntity.status(e.getErrorCode()).body(response);
	}

	// @Valid 예외 처리 (@NotNull, @Size, etc...) or IllegalArgumentException
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionResponse<Void>> handle(MethodArgumentNotValidException e) {

		logWarning(e, ERROR_CODE);
		ExceptionResponse<Void> response = ExceptionResponse.fail(ERROR_CODE,
			e.getBindingResult().getAllErrors().get(0).getDefaultMessage());

		return ResponseEntity
			.status(ERROR_CODE)
			.body(response);
	}

	// 서버 측 에러 (이외의 에러)
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse<Void>> handle(Exception e) {

		logWarning(e, SERVER_ERROR_CODE);
		ExceptionResponse<Void> response = ExceptionResponse.fail(SERVER_ERROR_CODE, e.getMessage());

		return ResponseEntity
			.status(SERVER_ERROR_CODE)
			.body(response);
	}

	@ExceptionHandler(AuthorizationDeniedException.class)
	public ResponseEntity<ApiResponse<?>> handleAuthorizationDeniedException() {
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
			.body(ApiResponse.error("권한이 없습니다.", 403));
	}

	@ExceptionHandler(MissingRequestCookieException.class)
	public ResponseEntity<ApiResponse<?>> handleMissingRequestCookie(MissingRequestCookieException e) {
		String cookieName = e.getCookieName();
		String message = String.format("필수 쿠키 '%s' 이/가 필요합니다.", cookieName);

		log.warn("Missing required cookie: {}", cookieName, e);

		return ResponseEntity
			.status(HttpStatus.UNAUTHORIZED)
			.body(ApiResponse.error(message, HttpStatus.UNAUTHORIZED.value()));
	}

	private void logWarning(Exception e, int errorCode) {
		log.warn(e.getMessage(), e);
		log.warn(LOG_FORMAT, e.getClass().getSimpleName(), errorCode, e.getMessage());
	}
}
