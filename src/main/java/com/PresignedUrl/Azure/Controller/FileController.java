package com.PresignedUrl.Azure.Controller;

import com.PresignedUrl.Azure.DTOs.PresignedUrlRequest;
import com.PresignedUrl.Azure.DTOs.PresignedUrlResponse;
import com.PresignedUrl.Azure.Service.FileUploadService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/files")
public class FileController {

    private final FileUploadService fileUploadService;

    public FileController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }


    @PostMapping("/generate-presigned-url")
    public ResponseEntity<PresignedUrlResponse> generatePresignedUrl(@Valid @RequestBody PresignedUrlRequest presignedUrlRequest) {
        PresignedUrlResponse response = fileUploadService.generatePresignedUrl(presignedUrlRequest);
        return ResponseEntity.ok(response);
    }

}
