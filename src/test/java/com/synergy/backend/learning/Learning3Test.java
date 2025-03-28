package com.synergy.backend.learning;


import com.synergy.backend.global.util.file.util.FileS3Util;
import com.synergy.backend.global.util.file.util.FileUtil;
import com.synergy.backend.module.IntegrationSupportTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

public class Learning3Test extends IntegrationSupportTest {


    @DisplayName("test task 수행 시간 검증 테스트")
    @Test
    void test1() {
        // given
        // when
        FileS3Util fileS3Util;
        // then
    }

}
