package com.synergy.backend.global.util.file.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.synergy.backend.global.util.file.dto.FileInformationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class FileS3UtilTest {

    @InjectMocks
    private FileS3Util fileS3Util;

    @Mock
    private AmazonS3 amazonS3;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(fileS3Util, "bucketName", "test-bucket"); // ✅ bucketName 수동 주입
    }

    @DisplayName("파일을 업로드합니다.")
    @Test
    void uploadFilesFrom() throws MalformedURLException {
        // given
        String fileName = "test-file";
        String contentType = "png";
        MockMultipartFile mockFile = new MockMultipartFile(fileName, fileName + contentType, contentType, "test content".getBytes());


        String TEST_URL = "https://test-bucket.s3.region.amazonaws.com/all/uuid" + fileName + contentType;
        given(amazonS3.getUrl(anyString(), anyString())).willReturn(new URL(TEST_URL));

        // when
        List<FileInformationDto> result = fileS3Util.uploadFilesFrom(List.of(mockFile));
        // then
        assertThat(result)
                .hasSize(1);

        verify(amazonS3, times(1)).putObject(any(PutObjectRequest.class));

    }


}