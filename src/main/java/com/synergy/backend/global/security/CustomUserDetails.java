package com.synergy.backend.global.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.synergy.backend.domain.member.entity.Admin;
import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.Recruiter;
import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.domain.member.entity.User;
import com.synergy.backend.global.security.exception.UnKnownUserTypeException;

import lombok.Getter;

@Getter
public class CustomUserDetails implements UserDetails {

	private final Long id;
	private final RoleType role;
	private final String identifier;
	private final String password;

	public CustomUserDetails(User user) {
		this.id = user.getId();
		this.role = user.getRole();

		if (user instanceof Attendee attendee) {
			this.identifier = attendee.getEmail();
			this.password = attendee.getPassword();
		} else if (user instanceof Admin admin) {
			this.identifier = admin.getAdminAuthCode();
			this.password = null;
		} else if (user instanceof Recruiter recruiter) {
			this.identifier = recruiter.getRecruiterAuthCode();
			this.password = null;
		} else {
			throw new UnKnownUserTypeException();
		}
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(role);
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return identifier;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
