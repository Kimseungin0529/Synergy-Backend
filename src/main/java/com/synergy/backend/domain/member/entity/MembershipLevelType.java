package com.synergy.backend.domain.member.entity;

public enum MembershipLevelType {
	PLATINUM,
	GOLD,
	SILVER,
	BRONZE,
	DEFAULT;

	public static MembershipLevelType getMembershipLevel(int point) {
		if (point >= 1500) return PLATINUM;
		if (point >= 800) return GOLD;
		if (point >= 300) return SILVER;
		if (point >= 100) return BRONZE;
		return DEFAULT;
	}
}
