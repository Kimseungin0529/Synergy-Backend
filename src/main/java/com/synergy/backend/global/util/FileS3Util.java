package com.synergy.backend.global.util;

import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileS3Util implements FileUtil{

    private final AmazonS3 s3Service;

    @Value("${spring.cloud.aws.s3.bucket}")
    private final String bucketName;

    @Override
    public FileResponseDto uploadFilesFrom(FileUploadRequestDto dto) {
        return null;
    }

    @Override
    public void deleteFilesFrom(FileDeleteRequestDto dto) {

    }

    @Override
    public FileResponseDto getFiles(FileListRequestDto fileKey) {
        return null;
    }
}
