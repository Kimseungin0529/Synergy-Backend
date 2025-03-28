package com.synergy.backend.learning;


import com.synergy.backend.global.config.MailConfig;
import com.synergy.backend.global.util.file.util.FileUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class Learning2Test {

    @MockitoBean
    JavaMailSender mailSender;

    @DisplayName("test task 수행 시간 검증 테스트")
    @Test
    void test1() {
        // given
        // when
        // then
    }

}
