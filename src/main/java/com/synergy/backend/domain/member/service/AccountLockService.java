package com.synergy.backend.domain.member.service;

import com.synergy.backend.domain.member.entity.User;

public interface AccountLockService {
	void lockUserAccount(User user);
}
