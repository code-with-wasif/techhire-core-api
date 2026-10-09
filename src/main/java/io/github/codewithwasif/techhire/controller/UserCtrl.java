package io.github.codewithwasif.techhire.controller;

import io.github.codewithwasif.techhire.dto.UserDto;
import io.github.codewithwasif.techhire.service.UserSvc;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
@Tag(
        name = "User Management",
        description = "Endpoints for user registration and login, including developer and employer sign-up.")
public class UserCtrl {

    private final UserSvc userSvc;

    @Operation(
            summary = "Developer sign-up",
            description = "Registers a new developer account by providing user details in the request body.")
    @PostMapping("/sign-up/dev")
    public ResponseEntity<UserDto.UserResponseDto> devSignUp(@Valid @RequestBody UserDto.CreateUserRequestDto userRequestDto){
        return userSvc.createDev(userRequestDto);
    }

    @Operation(
            summary = "Employer sign-up",
            description = "Registers a new employer account by providing user details in the request body.")
    @PostMapping("/sign-up/emp")
    public ResponseEntity<UserDto.UserResponseDto> empSignUp(@Valid @RequestBody UserDto.CreateUserRequestDto userRequestDto){
        return userSvc.createEmp(userRequestDto);
    }

    @Operation(
            summary = "User login",
            description = "Authenticates a user by verifying their username and password, returning a token if successful.")
    @PostMapping("/login")
    public ResponseEntity<String> loginDev(@RequestBody UserDto.LoginUserRequestDto userRequestDto){
        return userSvc.login(userRequestDto);
    }
}
