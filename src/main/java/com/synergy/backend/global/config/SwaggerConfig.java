package com.synergy.backend.global.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.method.HandlerMethod;

import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.global.annotation.DisableSwaggerSecurity;
import com.synergy.backend.global.annotation.SwaggerSummaryRole;

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
		return new OpenAPI().info(new Info().title("Synergy Backend API")
				.version("1.0")
				.description("Synergy 프로젝트의 API 문서입니다.")
				.license(new License().name("Synergy").url("https://github.com/Goorm-Synergy/Synergy-Backend")))
			.addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
			.components(new io.swagger.v3.oas.models.Components().addSecuritySchemes("BearerAuth",
				new SecurityScheme().name("BearerAuth")
					.type(SecurityScheme.Type.HTTP)
					.scheme("bearer")
					.bearerFormat("JWT")));
	}

	@Bean
	@Primary
	public OperationCustomizer swaggerOperationCustomizer() {
		return (operation, handlerMethod) -> {
			// Disable security
			if (handlerMethod.hasMethodAnnotation(DisableSwaggerSecurity.class)) {
				operation.setSecurity(Collections.emptyList());
			}

			// Add role-based summary
			SwaggerSummaryRole roleAnnotation = handlerMethod.getMethodAnnotation(SwaggerSummaryRole.class);
			if (roleAnnotation != null) {
				String originalSummary = operation.getSummary() != null ? operation.getSummary() : "";
				String rolePrefix = Arrays.stream(roleAnnotation.value())
					.map(RoleType::name)
					.collect(Collectors.joining(", ", "[", "] "));
				operation.setSummary(rolePrefix + originalSummary);
			}

			return operation;
		};
	}

	@Bean
	public GroupedOpenApi allApis(OperationCustomizer customize) {
		return GroupedOpenApi.builder()
			.group("전체 API 보기")
			.pathsToMatch("/api/**")
			.packagesToExclude("com.synergy.backend.domain.meta")
			.addOperationCustomizer(customize)
			.build();
	}

	@Bean
	public GroupedOpenApi frontendOnlyApi(OperationCustomizer customize) {
		return GroupedOpenApi.builder()
			.group("프론트엔드 전용 API")
			.packagesToScan("com.synergy.backend.domain.meta")
			.addOperationCustomizer(customize)
			.build();
	}
}
