//package com.synergy.backend.domain.member.repository;
//
//import com.synergy.backend.domain.member.entity.Admin;
//import com.synergy.backend.module.IntegrationSupportTest;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//
//class AdminRepositoryTest extends IntegrationSupportTest {
//    @Autowired
//    AdminRepository adminRepository;
//
//    @DisplayName("인증 코드가 같은 관리자를 조회합니다.")
//    @Test
//    void findByAdminAuthCode() {
//        // given
//        String authCode = "ADM12345";
//        // when
//        Optional<Admin> optionalResult = adminRepository.findByAdminAuthCode(authCode);
//        // then
//        assertThat(optionalResult.isPresent()).isTrue();
//
//        Admin result = optionalResult.get();
//        assertThat(result)
//                .extracting(Admin::getAdminAuthCode)
//                .isEqualTo(authCode);
//    }
//
//    @DisplayName("해당 conferenceId에 대한 컨퍼런스를 가진 관리자에 대해 adminId, conferenceId로 관리자를 조회합니다.")
//    @Test
//    void findByIdAndConferences_Id() {
//        // given
//        Long adminId = 1L;
//        Long conferenceId = 1L;
//        // when
//        Optional<Admin> optionalResult = adminRepository.findByIdAndConferences_Id(adminId, conferenceId);
//        // then
//        assertThat(optionalResult.isPresent()).isTrue();
//
//        Admin result = optionalResult.get();
//        assertThat(result)
//                .extracting(Admin::getId)
//                .isEqualTo(adminId);
//    }
//
//    @DisplayName("adminId에 해당하는 관리자는 conferenceId에 대한 컨퍼런스를 가지지 않습니다.")
//    @Test
//    void findByIdAndConferences_IdExceptionCase() {
//        // given
//        Long adminId = 1L;
//        Long conferenceId = 3L;
//        // when
//        Optional<Admin> optionalResult = adminRepository.findByIdAndConferences_Id(adminId, conferenceId);
//        // then
//        assertThat(optionalResult.isPresent()).isFalse();
//
//    }
//
//
//}