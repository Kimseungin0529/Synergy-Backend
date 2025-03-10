package com.synergy.backend.global.util;

import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FileS3Util implements FileUtil{

    private final AmazonS3 s3Service;

    @Value("${spring.cloud.aws.s3.bucket}")
    private final String bucketName;

    @Override
    public List<String> uploadFilesFrom(List<MultipartFile> files) {
        return List.of();
    }

    @Override
    public void deleteFilesFrom(List<String> fileKeys) {

    }

    @Override
    public List<String> getFilesFrom(List<String> fileKeys) {
        return List.of();
    }
}
