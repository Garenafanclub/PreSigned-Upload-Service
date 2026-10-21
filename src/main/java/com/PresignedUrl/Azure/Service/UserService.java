package com.PresignedUrl.Azure.Service;

import com.PresignedUrl.Azure.DTOs.CreateUserRequest;
import com.PresignedUrl.Azure.DTOs.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(CreateUserRequest createUserRequest);

    List<UserResponse> getAllUsers();
}
