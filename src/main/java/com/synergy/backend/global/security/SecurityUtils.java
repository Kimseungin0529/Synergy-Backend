package com.synergy.backend.global.security;

import com.synergy.backend.domain.member.entity.User;
import com.synergy.backend.global.exception.AuthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtils {

    public static User getCurrentMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Current user: {}", authentication.getPrincipal());
        log.info("Current username: {}", authentication.getName());

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthorizedException();
        }

        Object principal = authentication.getPrincipal();

        return (User) principal;
    }
}
