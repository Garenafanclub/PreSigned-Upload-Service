package com.PresignedUrl.aws.dto.response;

import java.util.UUID;

public record PresignedUrlResponse (
        String s3Key,
        String presignedUrl,
        UUID fileId
) {}
