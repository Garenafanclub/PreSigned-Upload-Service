package com.PresignedUrl.Azure.Mapper;

import com.PresignedUrl.Azure.DTOs.CreateUserRequest;
import com.PresignedUrl.Azure.DTOs.CreateUserResponse;
import com.PresignedUrl.Azure.Model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // CONVERT DTO TO ENTITY
    @Mapping(target = "userId", ignore = true)
    User toEntity(CreateUserRequest createUserRequest);

    // CONVERT ENTITY TO DTO
    CreateUserResponse toResponseDTO(User user);
}
