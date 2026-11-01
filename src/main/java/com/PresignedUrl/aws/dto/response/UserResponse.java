package com.PresignedUrl.aws.dto.response;

public record UserResponse(
    Long userId,
    String name,
    String email
) {}
