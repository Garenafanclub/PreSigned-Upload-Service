package com.PresignedUrl.Azure.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PresignedUrlRequest(
        @NotNull
        Long userId,

        @NotBlank
        String fileName,

        @NotBlank
        String contentType,

        @Positive
        Long fileSizeBytes
) {}
