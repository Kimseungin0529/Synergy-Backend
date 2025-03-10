package com.synergy.backend.global.util;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUtil {
    public FileResponseDto uploadFilesFrom(FileUploadRequestDto dto);

    public void deleteFilesFrom(FileDeleteRequestDto dto);

    public FileResponseDto getFiles(FileListRequestDto fileKey);

}
