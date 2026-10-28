package com.PresignedUrl.Azure.Service.Imp;

import com.PresignedUrl.Azure.DTOs.PresignedUrlRequest;
import com.PresignedUrl.Azure.DTOs.PresignedUrlResponse;
import com.PresignedUrl.Azure.Service.FileUploadService;
import org.springframework.stereotype.Service;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Override
    public PresignedUrlResponse generatePresignedUrl(PresignedUrlRequest presignedUrlRequest) {
        return null;
    }
}
