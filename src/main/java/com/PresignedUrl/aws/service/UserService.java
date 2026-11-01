package com.PresignedUrl.aws.service;

import com.PresignedUrl.aws.dto.request.CreateUserRequest;
import com.PresignedUrl.aws.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(CreateUserRequest createUserRequest);

    List<UserResponse> getAllUsers();
}
