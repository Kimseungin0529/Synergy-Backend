package com.synergy.backend.global.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUtil {
    List<FileInformationDto> uploadFilesFrom(List<MultipartFile> files);

    List<FileAccessDto> getFilesFrom(List<String> fileKeys);

    void deleteFilesFrom(List<String> fileKeys);


}
