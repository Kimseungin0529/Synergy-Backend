package com.synergy.backend.global.util.file.exception;

import com.synergy.backend.global.exception.BaseErrorException;

import static com.synergy.backend.global.util.file.exception.ErrorType._FILE_UPLOAD_FAIL;

public class FileUploadS3Exception extends BaseErrorException {
    public FileUploadS3Exception() {
        super(_FILE_UPLOAD_FAIL.getCode(), _FILE_UPLOAD_FAIL.getMessage());
    }
}
