package io.github.codewithwasif.techhire.mapper;

import io.github.codewithwasif.techhire.dto.UserDto;
import io.github.codewithwasif.techhire.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public UserEntity toEntity(UserDto.CreateUserRequest userRequestDto, String encodedPassword, List<String> role){
        return UserEntity.builder().userName(userRequestDto.userName())
                .email(userRequestDto.email())
                .password(encodedPassword)
                .roles(role)
                .build();
    }

    public UserDto.UserResponse toResponse(UserEntity user){
        return UserDto.UserResponse.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
