package com.PresignedUrl.aws.service;

import com.PresignedUrl.aws.dto.request.PresignedUrlRequest;
import com.PresignedUrl.aws.dto.response.PresignedUrlResponse;

public interface FileUploadService {
    PresignedUrlResponse generatePresignedUrl(PresignedUrlRequest presignedUrlRequest);
}
