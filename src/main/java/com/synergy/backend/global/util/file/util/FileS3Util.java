package com.synergy.backend.global.util.file.util;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.synergy.backend.global.util.file.dto.FileAccessDto;
import com.synergy.backend.global.util.file.dto.FileInformationDto;
import com.synergy.backend.global.util.file.exception.ExternalApiCallException;
import com.synergy.backend.global.util.file.exception.FileUploadS3Exception;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.FileInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileS3Util implements FileUtil {

    private final AmazonS3 amazonS3Service;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    private static final String PREFIX = "all/";
    private static final String QR = "qr/";

    @Override
    public FileInformationDto uploadQRCode(byte[] qrCode, String fileName) {
        String accessKey = generateUniqueFileNameFrom(QR, fileName);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(qrCode.length);
        metadata.setContentType("image/png");

        ByteArrayInputStream inputStream = new ByteArrayInputStream(qrCode);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, accessKey, inputStream, metadata);

        amazonS3Service.putObject(putObjectRequest);
        String accessUrl =  amazonS3Service.getUrl(bucketName, accessKey).toString();

        return new FileInformationDto(accessKey, accessUrl);
    }

    @Override
    public List<FileInformationDto> uploadFilesFrom(List<MultipartFile> files) {
        List<FileInformationDto> fileInformation = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                // 고유한 파일 이름 생성
                String fileKey = generateUniqueFileNameFrom(PREFIX, file.getOriginalFilename());

                // 메타데이터 설정
                ObjectMetadata metadata = generateMetadata(file);

                // S3에 파일 업로드 요청 생성
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileKey, file.getInputStream(), metadata);

                // S3에 파일 업로드
                amazonS3Service.putObject(putObjectRequest);
                String accessUrl = amazonS3Service.getUrl(bucketName, fileKey).toString();
                fileInformation.add(new FileInformationDto(fileKey, accessUrl));

            } catch (Exception e) {
                log.error("파일 업로드 ERROR = {}", e.getMessage(), e);
                throw new FileUploadS3Exception();
            }
        }

        return fileInformation;
    }

    @Override
    public void deleteFilesFrom(List<String> keys) {
        try {
            if (keys.isEmpty()) {
                return;
            }
            DeleteObjectsRequest deleteRequest = new DeleteObjectsRequest(bucketName)
                    .withKeys(keys.toArray(new String[0]));

            amazonS3Service.deleteObjects(deleteRequest);

        } catch (AmazonServiceException e) {
            throw new ExternalApiCallException("S3 다중 파일 삭제 실패 : " + e.getMessage());
        } catch (SdkClientException e) {
            throw new ExternalApiCallException("AWS 연결 오류로 파일 삭제 실패 : " + e.getMessage());
        }


    }

    @Override
    public List<FileAccessDto> getFilesFrom(List<String> keys) {
        List<FileAccessDto> accessUrls = new ArrayList<>();
        for (String fileKey : keys) {
            String accessUrl = amazonS3Service.getUrl(bucketName, fileKey).toString();
            accessUrls.add(new FileAccessDto(accessUrl));
        }

        return accessUrls;
    }

    private ObjectMetadata generateMetadata(MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        return metadata;
    }

    private String generateUniqueFileNameFrom(String prefix, String originalFilename) {
        return prefix + UUID.randomUUID() + "_" + originalFilename;
    }
}
