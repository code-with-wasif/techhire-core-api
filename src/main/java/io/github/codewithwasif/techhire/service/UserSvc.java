package io.github.codewithwasif.techhire.service;

import io.github.codewithwasif.techhire.dto.UserDto;
import io.github.codewithwasif.techhire.entity.UserEntity;
import io.github.codewithwasif.techhire.repository.UserRepo;
import io.github.codewithwasif.techhire.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSvc {

    private final UserRepo userRepo;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public UserDto.UserResponse createDev(UserDto.CreateUserRequest userRequestDto){

            UserEntity user = UserEntity.builder().userName(userRequestDto.userName())
                    .email(userRequestDto.email())
                    .password(passwordEncoder.encode(userRequestDto.password()))
                    .roles(List.of("DEVELOPER"))
                    .build();
            userRepo.save(user);

        return UserDto.UserResponse.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .build();
        }

    public UserDto.UserResponse createEmp(UserDto.CreateUserRequest userRequestDto){

            UserEntity user = UserEntity.builder().userName(userRequestDto.userName())
                    .email(userRequestDto.email())
                    .password(passwordEncoder.encode(userRequestDto.password()))
                    .roles(List.of("EMPLOYER"))
                    .build();
            userRepo.save(user);

        return UserDto.UserResponse.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .build();
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

