package com.synergy.backend.domain.member.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.synergy.backend.domain.auth.AccountLockedEvent;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.User;
import com.synergy.backend.domain.member.exception.NotFoundUserException;
import com.synergy.backend.domain.member.repository.AttendeeRepository;
import com.synergy.backend.global.mail.MailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountLockServiceImpl implements AccountLockService {

	private final AttendeeRepository attendeeRepository;
	private final ApplicationEventPublisher applicationEventPublisher;

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void lockUserAccount(User user) {
        /*
            계정 잠금 조치
                - 계정 islocked를 true 로 변경 (추가적인 인증 필요한 상태)
                - 계정 가입한 이메일로 보안 코드 발송
         */

		//  트랜잭션 안에서 영속성 컨텍스트에 관리되는 객체
		// REQUIRES_NEW: 별도 트랜잭션이므로 전달받은 객체가 deteched일 수 있음
		Attendee attendee = attendeeRepository.findById(user.getId())
			.orElseThrow(NotFoundUserException::new);

		attendee.lockAccount();
		attendeeRepository.save(attendee);

		// 이벤트 발행
		applicationEventPublisher.publishEvent(new AccountLockedEvent(attendee.getEmail()));
	}
}
