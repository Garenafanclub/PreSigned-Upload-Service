package com.PresignedUrl.aws.mapper;

import com.PresignedUrl.aws.dto.request.CreateUserRequest;
import com.PresignedUrl.aws.dto.response.UserResponse;
import com.PresignedUrl.aws.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // CONVERT DTO TO ENTITY
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    User toEntity(CreateUserRequest createUserRequest);

    // CONVERT ENTITY TO DTO
    UserResponse toResponseDTO(User user);
}
