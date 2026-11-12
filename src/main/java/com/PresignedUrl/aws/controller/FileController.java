package com.PresignedUrl.aws.controller;

import com.PresignedUrl.aws.dto.request.PresignedUrlRequest;
import com.PresignedUrl.aws.dto.response.PresignedUrlResponse;
import com.PresignedUrl.aws.service.FileUploadService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

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

    @GetMapping("/{fileId}/download-url")
    public ResponseEntity<Map<String, String>> generatePresignedUrlForDownload(
            @PathVariable UUID fileId, @RequestParam Long userId){
        String downloadUrl = fileUploadService.generateUrlForDownload(fileId, userId);
        return ResponseEntity.ok(Map.of("url", downloadUrl));
    }

}
