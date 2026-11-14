package com.PresignedUrl.aws.service;

import com.PresignedUrl.aws.dto.request.PresignedUrlRequest;
import com.PresignedUrl.aws.dto.response.PresignedUrlResponse;

import java.util.UUID;

public interface FileUploadService {
    PresignedUrlResponse generatePresignedUrl(PresignedUrlRequest presignedUrlRequest);

    String generateUrlForDownload(UUID fileId, Long userId);

    void markUploadAsComplete(String s3Key);
}
