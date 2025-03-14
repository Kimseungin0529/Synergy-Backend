package com.synergy.backend.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import com.synergy.backend.domain.member.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
	Optional<Admin> findByAdminAuthCode(String adminAuthCode);
}
