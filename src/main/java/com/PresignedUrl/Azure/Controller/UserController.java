package com.PresignedUrl.Azure.Controller;

import com.PresignedUrl.Azure.DTOs.CreateUserRequest;
import com.PresignedUrl.Azure.DTOs.UserResponse;
import com.PresignedUrl.Azure.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody  CreateUserRequest createUserRequest){
        return ResponseEntity.ok(userService.createUser(createUserRequest));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getUser()
    {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
