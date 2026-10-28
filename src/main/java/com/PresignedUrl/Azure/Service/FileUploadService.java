package com.PresignedUrl.Azure.Service;

import com.PresignedUrl.Azure.DTOs.PresignedUrlRequest;
import com.PresignedUrl.Azure.DTOs.PresignedUrlResponse;

public interface FileUploadService {
    PresignedUrlResponse generatePresignedUrl(PresignedUrlRequest presignedUrlRequest);
}
