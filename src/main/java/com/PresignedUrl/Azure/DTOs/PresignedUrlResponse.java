package com.PresignedUrl.Azure.DTOs;

import java.util.UUID;

public record PresignedUrlResponse (
        String s3Key,
        String presignedUrl,
        UUID fileId
) {}
