package com.synergy.backend.module;

import com.synergy.backend.global.util.file.util.FileS3Util;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public abstract class IntegrationSupportTest {

    @MockitoBean
    protected JavaMailSender mailSender;
    @MockitoBean
    protected FileS3Util fileS3Util;
}
