package com.PresignedUrl.Azure.DTOs;

public record CreateUserResponse(
    Long userId,
    String name,
    String email
) {}
