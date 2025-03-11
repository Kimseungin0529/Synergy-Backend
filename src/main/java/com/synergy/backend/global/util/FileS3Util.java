package com.synergy.backend.global.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FileS3Util implements FileUtil {

    private final AmazonS3 amazonS3Service;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    private static final String PREFIX = "all/";

    @Override
    public List<String> uploadFilesFrom(List<MultipartFile> files) {
        List<String> fileAccessKeys = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                String fileName = PREFIX + UUID.randomUUID() + "_" + file.getOriginalFilename(); // 고유한 파일 이름 생성

                // 메타데이터 설정
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(file.getContentType());
                metadata.setContentLength(file.getSize());

                // S3에 파일 업로드 요청 생성
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, file.getInputStream(), metadata);

                // S3에 파일 업로드
                amazonS3Service.putObject(putObjectRequest);
                fileAccessKeys.add(fileName);

                fileAccessKeys.add(getPublicUrl(fileName));
            } catch (Exception e) {
                throw new FileUploadS3Exception();
            }
        }

        return fileAccessKeys;
    }

    private String getPublicUrl(String fileName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, amazonS3Service.getRegionName(), fileName);
    }

    @Override
    public void deleteFilesFrom(List<String> fileKeys) {

    }

    @Override
    public List<String> getFilesFrom(List<String> fileKeys) {
        return List.of();
    }
}
