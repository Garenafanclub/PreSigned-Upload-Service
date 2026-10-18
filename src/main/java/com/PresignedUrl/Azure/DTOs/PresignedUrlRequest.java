package com.PresignedUrl.Azure.DTOs;

public record PresignedUrlRequest(
        Long userId,
        String fileName,
        String contentType
) {}
