package io.github.codewithwasif.techhire.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
public final class UserDto {
    private UserDto(){}

    public record CreateUserRequestDto(
            @NotBlank(message = "Username cannot be empty")
            @Size(min = 2, max = 20, message = "Username must be between 2 and 20 characters")
            String userName,

            @NotBlank(message = "Email is required")
            @Email
            String email,

            @NotBlank(message = "Password is required")
            @Size(min = 8, message = "Password must be at least 8 characters long")
            String password) {}



    public record LoginUserRequestDto(
            @NotBlank(message = "Username cannot be empty")
            String userName,

            @NotBlank(message = "Password is required")
            String password) {}


    @Builder
    public record UserResponseDto(
            String userName,
            String email) {}
}
