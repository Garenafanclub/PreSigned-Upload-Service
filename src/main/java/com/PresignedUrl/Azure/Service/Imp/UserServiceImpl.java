package com.PresignedUrl.Azure.Service.Imp;

import com.PresignedUrl.Azure.DTOs.CreateUserRequest;
import com.PresignedUrl.Azure.DTOs.CreateUserResponse;
import com.PresignedUrl.Azure.Mapper.UserMapper;
import com.PresignedUrl.Azure.Model.User;
import com.PresignedUrl.Azure.Repository.UserRepo;
import com.PresignedUrl.Azure.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    public CreateUserResponse createUser(CreateUserRequest createUserRequest) {
        log.info("Creating user with email: {}", createUserRequest.email());

        if(userRepo.findByEmail(createUserRequest.email()) != null){
            log.error("User with email {} already exists", createUserRequest.email());
            throw new RuntimeException("User with email " + createUserRequest.email() + " already exists");
        }

        User newUser = userMapper.toEntity(createUserRequest);
        userRepo.save(newUser);
        log.info("User with email {} created successfully", createUserRequest.email());

        return userMapper.toResponseDTO(newUser);
    }

    @Override
    public List<CreateUserResponse> getAllUsers() {
        // GET THE ALL USERS FROM THE DATABASE...
        List<User> users = userRepo.findAll();
        List<CreateUserResponse> userResponses = new ArrayList<>();
        for(User user : users)
        {
            CreateUserResponse response = userMapper.toResponseDTO(user);
            userResponses.add(response);
        }
        return userResponses;
    }
}
