package com.synergy.backend.global.config;

import java.util.Collections;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import com.synergy.backend.global.annotation.DisableSwaggerSecurity;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
			.info(new Info()
				.title("Synergy Backend API")
				.version("1.0")
				.description("Synergy 프로젝트의 API 문서입니다.")
				.license(new License()
					.name("Synergy")
					.url("https://github.com/Goorm-Synergy/Synergy-Backend")
				)
			)
			.addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
			.components(new io.swagger.v3.oas.models.Components()
				.addSecuritySchemes("BearerAuth",
					new SecurityScheme()
						.name("BearerAuth")
						.type(SecurityScheme.Type.HTTP)
						.scheme("bearer")
						.bearerFormat("JWT")
				));
	}

	@Bean
	public OperationCustomizer customize() {
		return (Operation operation, HandlerMethod handlerMethod) -> {
			DisableSwaggerSecurity methodAnnotation =
				handlerMethod.getMethodAnnotation(DisableSwaggerSecurity.class);
			// DisableSecurity 어노테이션있을시 스웨거 시큐리티 설정 삭제
			if (methodAnnotation != null) {
				operation.setSecurity(Collections.emptyList());
			}

			return operation;
		};
	}
}
