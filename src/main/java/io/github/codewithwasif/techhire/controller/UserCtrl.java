package io.github.codewithwasif.techhire.controller;
import io.github.codewithwasif.techhire.dto.UserDto;
import io.github.codewithwasif.techhire.service.UserSvc;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<UserDto> devSignUp(@Valid @RequestBody UserDto userDto){
        return userSvc.createDev(userDto);
    }

    @Operation(
            summary = "Employer sign-up",
            description = "Registers a new employer account by providing user details in the request body.")
    @PostMapping("/sign-up/emp")
    public ResponseEntity<UserDto> empSignUp(@Valid @RequestBody UserDto userDto){
        return userSvc.createEmp(userDto);
    }

    @Operation(
            summary = "User login",
            description = "Authenticates a user by verifying their username and password, returning a token if successful.")
    @PostMapping("/login")
    public ResponseEntity<String> loginDev(@RequestBody UserDto userDto){
        return userSvc.login(userDto);
    }
}
