package com.synergy.backend.domain.point.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.synergy.backend.domain.booth.entity.Booth;
import com.synergy.backend.domain.booth.exception.NotFoundBoothException;
import com.synergy.backend.domain.booth.repository.BoothRepository;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.Recruiter;
import com.synergy.backend.domain.member.exception.NotFoundUserException;
import com.synergy.backend.domain.member.repository.AttendeeRepository;
import com.synergy.backend.domain.member.repository.RecruiterRepository;
import com.synergy.backend.domain.point.api.dto.PointResponseDto;
import com.synergy.backend.domain.point.entity.Point;
import com.synergy.backend.domain.point.entity.PointType;
import com.synergy.backend.domain.point.exception.PointNotFoundException;
import com.synergy.backend.domain.point.repository.PointRepository;
import com.synergy.backend.domain.session.entity.Session;
import com.synergy.backend.domain.session.exception.NotFoundSession;
import com.synergy.backend.domain.session.repository.sessionRepository.SessionRepository;
import com.synergy.backend.global.security.exception.UnKnownUserTypeException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

	private final PointRepository pointRepository;
	private final AttendeeRepository attendeeRepository;
	private final BoothRepository boothRepository;
	private final SessionRepository sessionRepository;
	private final RecruiterRepository recruiterRepository;

	@Transactional(readOnly = true)
	@Override
	public List<Point> getPointHistory(Long attendeeId) {
		return pointRepository.findByAttendeeIdOrderByCreatedTimeDesc(attendeeId);
	}

	@Transactional(readOnly = true)
	@Override
	public PointResponseDto getPointResponse(Long pointId) {
		Point point = pointRepository.findById(pointId)
			.orElseThrow(PointNotFoundException::new);

		String details = getDetailsForPoint(point);
		return PointResponseDto.from(point, details);
	}

	@Transactional(readOnly = true)
	@Override
	public List<PointResponseDto> getPointResponses(Long attendeeId) {
		return getPointHistory(attendeeId).stream()
			.map(point -> getPointResponse(point.getId()))
			.collect(Collectors.toList());
	}

	@Transactional
	@Override
	public void addBoothPoint(Long attendeeId, Long boothId) {
		addPoint(attendeeId, PointType.BOOTH_VISIT, boothId);
	}

	@Transactional
	@Override
	public void addSessionAttendPoint(Long attendeeId, Long sessionId) {
		addPoint(attendeeId, PointType.SESSION_ATTEND, sessionId);
	}

	@Transactional
	@Override
	public void addSessionQnaPoint(Long attendeeId, Long sessionId) {
		addPoint(attendeeId, PointType.SESSION_QNA, sessionId);
	}

	@Transactional
	@Override
	public void addSignupPoint(Long attendeeId) {
		addPoint(attendeeId, PointType.SIGN_UP, null);
	}

	@Transactional
	public void addPoint(Long attendeeId, PointType pointType, Long detailId) {
		Attendee attendee = attendeeRepository.findById(attendeeId).orElseThrow(UnKnownUserTypeException::new);

		Point point = Point.builder()
			.pointType(pointType)
			.build();

		if (detailId != null) {
			updatePointDetail(point, pointType, detailId);
		}

		attendee.addPoint(point);
		pointRepository.save(point);

		int pointValue = pointType.getPointValue();
		attendee.addTotalPoints(pointValue);
		attendeeRepository.save(attendee);

	}

	@Override
	public String getDetailsForPoint(Point point) {
		return switch (point.getPointType()) {
			case BOOTH_VISIT -> {
				if (point.getBoothId() != null) {
					Booth booth = boothRepository.findById(point.getBoothId())
						.orElseThrow(() -> new NotFoundBoothException("부스 정보를 찾을 수 없습니다."));
					yield booth.getCompanyName();
				}
				yield "";
			}
			case SESSION_ATTEND, SESSION_QNA -> {
				if (point.getSessionId() != null) {
					Session session = sessionRepository.findById(point.getSessionId())
						.orElseThrow(NotFoundSession::new);
					yield session.getTitle();
				}
				yield "";
			}
			case RECRUITER_MEETING -> {
				if (point.getRecruiterId() != null) {
					Recruiter recruiter = recruiterRepository.findById(point.getRecruiterId())
						.orElseThrow(NotFoundUserException::new);
					yield recruiter.getCompany();
				}
				yield "";
			}
			case SIGN_UP -> "회원가입 적립";
			case SURVEY_PARTICIPATION -> "설문조사 참여 적립";
			case CONTENT_SHARE -> "컨텐츠 공유 적립";
		};
	}

	private void updatePointDetail(Point point, PointType pointType, Long detailId) {
		switch (pointType) {
			case BOOTH_VISIT -> point.updateBoothId(detailId);
			case SESSION_ATTEND, SESSION_QNA -> point.updateSessionId(detailId);
			case RECRUITER_MEETING -> point.updateRecruiterId(detailId);
			default -> {
			}
		}
	}

}
