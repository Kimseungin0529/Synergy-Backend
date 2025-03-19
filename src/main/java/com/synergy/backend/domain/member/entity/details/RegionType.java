package com.synergy.backend.domain.member.entity.details;

import java.util.function.Supplier;

import com.synergy.backend.domain.member.exception.InvalidRegionTypeCodeException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RegionType implements BaseAttendeeDetailEnum {
	CAPITAL_AREA(11, "수도권 (서울특별시, 인천광역시, 경기도)"),
	BUSAN(12, "부산광역시"),
	DAEGU(13, "대구광역시"),
	DAEJEON(14, "대전광역시"),
	GWANGJU(15, "광주광역시"),
	ULSAN(16, "울산광역시"),
	SEJONG(17, "세종특별자치시"),
	GANGWON(18, "강원권 (강원특별자치도)"),
	CHUNGCHEONG(19, "충청권 (충청북도, 충청남도)"),
	JEOLLA(20, "전라권 (전라북도, 전라남도)"),
	GYEONGSANG(21, "경상권 (경상북도, 경상남도)"),
	JEJU(22, "제주특별자치도"),
	;

	private final Integer code;
	private final String description;
	private final Supplier<? extends RuntimeException> exceptionSupplier = InvalidRegionTypeCodeException::new;

	public static RegionType fromCode(int code) {
		return BaseAttendeeDetailEnum.fromCode(RegionType.class, code);
	}
}
