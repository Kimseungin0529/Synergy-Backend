package com.synergy.backend.domain.member.entity.details;

import com.synergy.backend.domain.member.vo.NextPointInfo;

public enum MembershipLevelType {
	PLATINUM,
	GOLD,
	SILVER,
	BRONZE,
	DEFAULT;

	private static final int BRONZE_THRESHOLD = 100;
	private static final int SILVER_THRESHOLD = 300;
	private static final int GOLD_THRESHOLD = 800;

	public static MembershipLevelType getMembershipLevel(int point) {
		if (point >= GOLD_THRESHOLD)
			return GOLD;
		if (point >= SILVER_THRESHOLD)
			return SILVER;
		if (point >= BRONZE_THRESHOLD)
			return BRONZE;
		return DEFAULT;
	}

	public static NextPointInfo getNextLevelInfo(int point) {
		if (point >= GOLD_THRESHOLD)
			return new NextPointInfo(PLATINUM, 0);
		if (point >= SILVER_THRESHOLD)
			return new NextPointInfo(GOLD, GOLD_THRESHOLD - point);
		if (point >= BRONZE_THRESHOLD)
			return new NextPointInfo(SILVER, SILVER_THRESHOLD - point);
		if (point >= 0)
			return new NextPointInfo(BRONZE, BRONZE_THRESHOLD - point);
		return new NextPointInfo(DEFAULT, 0);
	}
}
