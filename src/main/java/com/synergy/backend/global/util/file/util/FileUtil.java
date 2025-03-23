package com.synergy.backend.global.util.file.util;

import com.synergy.backend.global.util.file.dto.FileAccessDto;
import com.synergy.backend.global.util.file.dto.FileInformationDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileUtil {

    FileInformationDto uploadQRCode(byte[] qrCode, String fileName);

    FileInformationDto uploadFile(MultipartFile file) throws IOException;

    List<FileInformationDto> uploadFilesFrom(List<MultipartFile> files);

    List<FileAccessDto> getFilesFrom(List<String> fileKeys);

    void deleteFilesFrom(List<String> fileKeys);


}
