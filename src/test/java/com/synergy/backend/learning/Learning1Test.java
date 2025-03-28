package com.synergy.backend.learning;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class Learning1Test {

    @DisplayName("test task 수행 시간 검증 테스트")
    @Test
    void test1() {
        // given
        // when
        // then
    }

}
