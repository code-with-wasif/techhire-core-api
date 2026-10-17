package io.github.codewithwasif.techhire.service;

import io.github.codewithwasif.techhire.dto.UserDto;
import io.github.codewithwasif.techhire.entity.UserEntity;
import io.github.codewithwasif.techhire.mapper.UserMapper;
import io.github.codewithwasif.techhire.repository.UserRepo;
import io.github.codewithwasif.techhire.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class UserSvc {

    private final UserRepo userRepo;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional
    public UserDto.UserResponse createDev(UserDto.CreateUserRequest userRequestDto){

        String password = passwordEncoder.encode(userRequestDto.password());
        List<String> roles = List.of("DEVELOPER");

        UserEntity user = userMapper.toEntity(userRequestDto, password, roles);

        UserEntity savedUser = userRepo.save(user);

        return userMapper.toResponse(savedUser);
    }

    @Transactional
    public UserDto.UserResponse createEmp(UserDto.CreateUserRequest userRequestDto){

        String password = passwordEncoder.encode(userRequestDto.password());
        List<String> roles = List.of("EMPLOYER");

        UserEntity user = userMapper.toEntity(userRequestDto, password, roles);

        UserEntity savedUser = userRepo.save(user);

        return userMapper.toResponse(savedUser);
    }

    public String login(UserDto.LoginRequest userRequestDto){

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    userRequestDto.userName(), userRequestDto.password()));

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            return jwtUtils.generateToken(userRequestDto.userName(), roles);

    }
}

