package com.synergy.backend.domain.member.vo;

import com.synergy.backend.domain.member.entity.details.MembershipLevelType;

public record NextPointInfo(MembershipLevelType nextLevel, int needPoint) {
}
