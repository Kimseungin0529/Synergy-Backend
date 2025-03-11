package com.synergy.backend.domain.point.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PointType {
	SIGN_UP(50, "회원가입"),
	BOOTH_VISIT(20, "부스 방문"),
	SESSION_ATTEND(30, "세션 참여"),
	SESSION_QNA(50, "세션 Q&A 참여"),
	SURVEY_PARTICIPATION(30, "설문 조사 참여"),
	CONTENT_SHARE(20, "컨텐츠 공유 (SNS)"),
	RECRUITER_MEETING(50, "채용 담당자 미팅");

	private final int pointValue;
	private final String message;
}
