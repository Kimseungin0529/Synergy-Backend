package com.synergy.backend.global.common;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class ApiResponseAdvice<T> implements ResponseBodyAdvice<ApiResponse<T>> {

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return ApiResponse.class.isAssignableFrom(returnType.getParameterType());
	}

	@Override
	public ApiResponse<T> beforeBodyWrite(ApiResponse<T> body, MethodParameter returnType,
		MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
		ServerHttpRequest request, ServerHttpResponse response) {

		int statusCode = 200;
		if (response instanceof ServletServerHttpResponse servletResponse) {
			statusCode = servletResponse.getServletResponse().getStatus();
		}

		return new ApiResponse<>(body.status(), statusCode, body.message(), body.data());
	}
}
