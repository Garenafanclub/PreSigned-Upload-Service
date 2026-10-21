package com.PresignedUrl.Azure.DTOs;

public record UserResponse(
    Long userId,
    String name,
    String email
) {}
