package com.synergy.backend.global.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUtil {
    List<String> uploadFilesFrom(List<MultipartFile> files);

    void deleteFilesFrom(List<String> fileKeys);

    List<String> getFilesFrom(List<String> fileKeys);

}
