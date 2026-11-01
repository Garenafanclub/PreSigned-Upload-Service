package com.PresignedUrl.aws.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest (
        @NotBlank
        @Size(max = 50)
        String name,

        @NotBlank
        @Size(max = 254)
        @Email
        String email
) {}
