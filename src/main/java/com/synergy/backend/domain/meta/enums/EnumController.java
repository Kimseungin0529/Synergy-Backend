package com.synergy.backend.domain.meta.enums;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.synergy.backend.global.annotation.DisableSwaggerSecurity;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "frontend 전용 API")
@RestController
@RequestMapping("/api/v1/frontend/codes")
public class EnumController {

	@DisableSwaggerSecurity
	@GetMapping
	public List<EnumResponseDto> getEnumOptions(@RequestParam EnumType type) {
		return type.getValues();
	}
}
