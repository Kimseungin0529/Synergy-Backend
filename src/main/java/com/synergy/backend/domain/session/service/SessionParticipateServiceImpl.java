package com.synergy.backend.domain.session.service;

import com.synergy.backend.domain.conference.entity.Conference;
import com.synergy.backend.domain.conference.exception.NotFoundConference;
import com.synergy.backend.domain.conference.repository.ConferenceRepository;
import com.synergy.backend.domain.member.entity.Admin;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.exception.NotFoundUserException;
import com.synergy.backend.domain.member.repository.AdminRepository;
import com.synergy.backend.domain.member.repository.AttendeeRepository;
import com.synergy.backend.domain.point.service.PointService;
import com.synergy.backend.domain.qrCode.service.QrService;
import com.synergy.backend.domain.session.dto.sessionDto.SessionResDto;
import com.synergy.backend.domain.session.dto.questionDto.QuestionReqDto;
import com.synergy.backend.domain.session.dto.sessionparticipateDto.SessionParticipateRateDetailResDto;
import com.synergy.backend.domain.session.dto.sessionparticipateDto.SessionParticipateRateResDto;
import com.synergy.backend.domain.session.dto.sessionparticipateDto.SessionParticipateTechResDto;
import com.synergy.backend.domain.session.entity.AttendeeSession;
import com.synergy.backend.domain.session.entity.Session;
import com.synergy.backend.domain.session.entity.SessionQuestion;
import com.synergy.backend.domain.session.exception.AlreadyAttendedException;
import com.synergy.backend.domain.session.exception.InvalidTimeException;
import com.synergy.backend.domain.session.exception.NotAttendedSession;
import com.synergy.backend.domain.session.exception.NotFoundSession;
import com.synergy.backend.domain.session.repository.AttendeeSessionRepository;
import com.synergy.backend.domain.session.repository.sessionQuestionRepository.SessionQuestionRepository;
import com.synergy.backend.domain.session.repository.sessionRepository.SessionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.synergy.backend.domain.member.entity.QAttendee.attendee;
import static com.synergy.backend.domain.session.entity.QSession.session;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionParticipateServiceImpl implements SessionParticipateService {

    private final QrService qrService;
    private final SessionRepository sessionRepository;
    private final SessionQuestionRepository sessionQuestionRepository;
    private final ConferenceRepository conferenceRepository;
    private final AttendeeSessionRepository attendeeSessionRepository;
    private final AttendeeRepository attendeeRepository;
    private final AdminRepository adminRepository;
    private final PointService pointService;

    @Transactional
    @Override
    public SessionResDto verifyQRCode(String identifier, Long sessionId, String secretCode) {
        Attendee currentMember = findIfAttendeeExists(identifier);
        secretCode = qrService.decodingSecretCode(secretCode);
        Session session = findBySecretCode(sessionId, secretCode);

        attendeeSessionRepository.findBySessionIdAndAttendeeId(sessionId, currentMember.getId())
                .ifPresent(attendeeSession -> {
                    throw new AlreadyAttendedException();
                });
        AttendeeSession attendeeSession = AttendeeSession.of(currentMember, session);
        attendeeSessionRepository.save(attendeeSession);

        pointService.addSessionAttendPoint(currentMember.getId(), session.getId());

        return SessionResDto.from(session);
    }

    @Transactional
    @Override
    public void createQuestion(String identifier, Long sessionId, QuestionReqDto reqDto) {
        Attendee attendee = findIfAttendeeExists(identifier);
        AttendeeSession attendeeSession = ifAttendeeSessionExists(sessionId, attendee.getId());

        SessionQuestion question = SessionQuestion.of(reqDto.content(), attendeeSession);
        sessionQuestionRepository.save(question);

        attendeeSession.addSessionQuestion(question);

        pointService.addSessionQnaPoint(attendee.getId(), sessionId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<SessionParticipateRateResDto> getSessionParticipateRate(String identifier, Long conferenceId) {
        Admin currentMember = findIfAdminExists(identifier);
        findIfConferenceMine(currentMember, conferenceId); // 해당 컨퍼런스의 소유자인지 확인해야돰.
        ifConferenceExists(conferenceId);

        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
        log.info("Today: {}", now);
        List<SessionParticipateRateResDto> sessionParticipate =
                sessionRepository.getSessionParticipateByConferenceId(conferenceId, now);
//        if(sessionParticipate.isEmpty()) {
//            throw new InvalidTimeException();
//        }

        return sessionParticipate;
    }

    @Transactional(readOnly = true)
    @Override
    public List<SessionParticipateRateDetailResDto> getSessionParticipateRateDetail(String identifier, Long conferenceId) {
        // 이름순으로 관심 분야를 기준으로 조회시키기.
        Admin currentMember = findIfAdminExists(identifier);
        findIfConferenceMine(currentMember, conferenceId); // 해당 컨퍼런스의 소유자인지 확인해야 됨.

        // 원본 세션 참여 정보
        List<SessionParticipateRateDetailResDto> rawList = sessionRepository.getSessionParticipateDetailByConferenceId(conferenceId);

        // 각 세션의 기술 리스트 후처리
        return rawList.stream()
                .map(this::refineTechList)
                .toList();
    }


    private SessionParticipateRateDetailResDto refineTechList(SessionParticipateRateDetailResDto dto) {
        List<SessionParticipateTechResDto> original = dto.dataset();

        if (original == null || original.size() <= 6) return dto;

        List<SessionParticipateTechResDto> sorted = original.stream()
                .sorted(Comparator.comparingLong(SessionParticipateTechResDto::attendeeCount).reversed())
                .toList();

        List<SessionParticipateTechResDto> top6 = new ArrayList<>(sorted.subList(0, 6));

        long etcCount = sorted.subList(6, sorted.size()).stream()
                .mapToLong(SessionParticipateTechResDto::attendeeCount)
                .sum();

        top6.add(new SessionParticipateTechResDto("기타", etcCount));

        // record 는 불변이라 새로 생성해야 함
        return new SessionParticipateRateDetailResDto(
                dto.sessionId(),
                dto.title(),
                dto.speaker(),
                dto.speakerPosition(),
                dto.description(),
                dto.maximum(),
                dto.progressDate(),
                dto.startDate(),
                dto.endDate(),
                dto.qrUrl(),
                top6
        );
    }

    private void findIfConferenceMine(Admin admin, Long conferenceId) {
        adminRepository.findByIdAndConferences_Id(admin.getId(), conferenceId);
    }

    private Admin findIfAdminExists(String identifier) {
        return adminRepository.findByAdminAuthCode(identifier).orElseThrow(NotFoundUserException::new);
    }

    private Attendee findIfAttendeeExists(String identifier) {
        return attendeeRepository.findByEmail(identifier).orElseThrow(NotFoundUserException::new);
    }

    private Session findBySecretCode(Long sessionId, String secretCode) {
        return sessionRepository.findByIdAndSecretCode(sessionId, secretCode).orElseThrow(NotFoundSession::new);
    }

    private AttendeeSession ifAttendeeSessionExists(Long sessionId, Long attendeeId) {
        return attendeeSessionRepository.findBySessionIdAndAttendeeId(sessionId, attendeeId)
                .orElseThrow(NotAttendedSession::new);
    }

    private Conference ifConferenceExists(Long conferenceId) {
        return conferenceRepository.findById(conferenceId).orElseThrow(NotFoundConference::new);
    }
}
