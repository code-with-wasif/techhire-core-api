package io.github.codewithwasif.techhire.service;

import io.github.codewithwasif.techhire.dto.UserDto;
import io.github.codewithwasif.techhire.entity.UserEntity;
import io.github.codewithwasif.techhire.mapper.UserMapper;
import io.github.codewithwasif.techhire.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class UserSvc {

    private static final String BEARER_TOKEN_TYPE = "Bearer";

    private final UserRepo userRepo;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional
    public UserDto.UserResponse createDev(UserDto.CreateUserRequest userRequestDto){

        String password = passwordEncoder.encode(userRequestDto.password());
        List<String> roles = List.of("ROLE_DEVELOPER");

        UserEntity user = userMapper.toEntity(userRequestDto, password, roles);

        UserEntity savedUser = userRepo.save(user);

        return userMapper.toResponse(savedUser);
    }

    @Transactional
    public UserDto.UserResponse createEmp(UserDto.CreateUserRequest userRequestDto){

        String password = passwordEncoder.encode(userRequestDto.password());
        List<String> roles = List.of("ROLE_EMPLOYER");

        UserEntity user = userMapper.toEntity(userRequestDto, password, roles);

        UserEntity savedUser = userRepo.save(user);

        return userMapper.toResponse(savedUser);
    }

    public UserDto.LoginResponse login(UserDto.LoginRequest loginRequest) {
        Authentication unauthenticatedToken = UsernamePasswordAuthenticationToken.unauthenticated(
                loginRequest.userName(),
                loginRequest.password()
        );

        Authentication authenticated = authenticationManager.authenticate(unauthenticatedToken);

        String accessToken = jwtService.generateToken(authenticated);

        return userMapper.toLoginResponse(accessToken, BEARER_TOKEN_TYPE ,jwtService.getExpirySeconds());
    }
}

