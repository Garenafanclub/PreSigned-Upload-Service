package com.PresignedUrl.aws.service.impl;

import com.PresignedUrl.aws.dto.request.CreateUserRequest;
import com.PresignedUrl.aws.dto.response.UserResponse;
import com.PresignedUrl.aws.mapper.UserMapper;
import com.PresignedUrl.aws.model.User;
import com.PresignedUrl.aws.repository.UserRepo;
import com.PresignedUrl.aws.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepo userRepo, UserMapper userMapper) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest createUserRequest) {
        log.info("Creating user with email: {}", createUserRequest.email());

        if (userRepo.findByEmail(createUserRequest.email()) != null) {
            log.error("User with email {} already exists", createUserRequest.email());
            // Throw a 409 Conflict instead of a generic 500 server error
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "User with email " + createUserRequest.email() + " already exists");
        }

        User newUser = userMapper.toEntity(createUserRequest);
        User savedUser = userRepo.save(newUser);
        log.info("User with email {} created successfully", createUserRequest.email());

        return userMapper.toResponseDTO(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        // GET THE ALL USERS FROM THE DATABASE...
        List<User> users = userRepo.findAll();
        List<UserResponse> userResponses = new ArrayList<>();
        for(User user : users)
        {
            UserResponse response = userMapper.toResponseDTO(user);
            userResponses.add(response);
        }
        return userResponses;
    }

    @Override
    public void deleteUser(String email) {
        log.info("Attempting to delete user with email: {}", email);
        User user = userRepo.findByEmail(email);
        if (user == null) {
            log.error("User with email {} not found", email);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "User with email " + email + " not found");
        }
        userRepo.delete(user);
        log.info("User with email {} deleted successfully", email);
    }
}
