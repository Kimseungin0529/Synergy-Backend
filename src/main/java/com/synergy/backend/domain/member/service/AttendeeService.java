package com.synergy.backend.domain.member.service;

import java.util.Set;

import com.synergy.backend.domain.interest.entity.Interest;
import com.synergy.backend.domain.member.entity.Attendee;

public interface AttendeeService {

	Set<Interest> addInterests(String email, Set<Long> interestIds);
}
