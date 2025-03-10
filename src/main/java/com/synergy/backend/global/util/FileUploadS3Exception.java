package com.synergy.backend.global.util;

import com.synergy.backend.global.exception.BaseErrorException;

import static com.synergy.backend.global.util.ErrorType._FILE_UPLOAD_FAIL;

public class FileUploadS3Exception extends BaseErrorException {
    public FileUploadS3Exception() {
        super(_FILE_UPLOAD_FAIL.getCode(), _FILE_UPLOAD_FAIL.getMessage());
    }
}
