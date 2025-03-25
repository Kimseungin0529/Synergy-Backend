package com.synergy.backend.global.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.synergy.backend.domain.member.entity.Attendee;
import com.synergy.backend.domain.member.entity.RoleType;
import com.synergy.backend.domain.member.entity.User;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

	private final User user;

	public User getUser() {
		return user;
	}

	public Long getId() {
		return user.getId();
	}

	public String getIdentifier() {
		return user.getIdentifier();
	}

	public RoleType getRole() {
		return user.getRole();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(user.getRole());
	}

	@Override
	public String getPassword() {

		if (user instanceof Attendee attendee) {
			return attendee.getPassword();
		}
		return null; // 혹은 "", 보안 정책에 따라
	}

	@Override
	public String getUsername() {
		return user.getIdentifier();
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
