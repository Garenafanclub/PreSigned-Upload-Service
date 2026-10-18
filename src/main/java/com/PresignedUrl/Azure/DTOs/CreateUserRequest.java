package com.PresignedUrl.Azure.DTOs;

public record CreateUserRequest (

        String name,
        String email,
        String address
) {}
