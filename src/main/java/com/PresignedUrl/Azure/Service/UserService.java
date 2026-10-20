package com.PresignedUrl.Azure.Service;

import com.PresignedUrl.Azure.DTOs.CreateUserRequest;
import com.PresignedUrl.Azure.DTOs.CreateUserResponse;

import java.util.List;

public interface UserService {
    CreateUserResponse createUser(CreateUserRequest createUserRequest);

    List<CreateUserResponse> getAllUsers();
}
