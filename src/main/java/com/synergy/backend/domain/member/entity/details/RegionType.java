package com.synergy.backend.domain.member.entity.details;

import java.util.function.Supplier;

import com.synergy.backend.domain.member.exception.InvalidRegionTypeCodeException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RegionType implements BaseAttendeeDetailEnum {
	CAPITAL_AREA(11, "수도권"),
	BUSAN(12, "부산광역시"),
	DAEGU(13, "대구광역시"),
	DAEJEON(14, "대전광역시"),
	GWANGJU(15, "광주광역시"),
	ULSAN(16, "울산광역시"),
	SEJONG(17, "세종특별자치시"),
	GANGWON(18, "강원권"),
	CHUNGCHEONG(19, "충청권"),
	JEOLLA(20, "전라권"),
	GYEONGSANG(21, "경상권"),
	JEJU(22, "제주특별자치도"),
	;

	private final Integer code;
	private final String description;
	private final Supplier<? extends RuntimeException> exceptionSupplier = InvalidRegionTypeCodeException::new;

	public static RegionType fromCode(int code) {
		return BaseAttendeeDetailEnum.fromCode(RegionType.class, code);
	}
}
