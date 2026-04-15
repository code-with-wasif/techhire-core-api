package io.github.codewithwasif.techhire.service;

import io.github.codewithwasif.techhire.dto.UserDto;
import io.github.codewithwasif.techhire.entity.UserEntity;
import io.github.codewithwasif.techhire.repository.UserRepo;
import io.github.codewithwasif.techhire.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    public ResponseEntity<UserDto> createDev(UserDto userDto){
        try {
            UserEntity user = UserEntity.builder().userName(userDto.getUserName())
                    .email(userDto.getEmail())
                    .password(passwordEncoder.encode(userDto.getPassword()))
                    .roles(List.of("DEVELOPER"))
                    .build();
            userRepo.save(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error while creating user {}",userDto.getUserName(),e);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<UserDto> createEmp(UserDto userDto){
        try {
            UserEntity user = UserEntity.builder().userName(userDto.getUserName())
                    .email(userDto.getEmail())
                    .password(passwordEncoder.encode(userDto.getPassword()))
                    .roles(List.of("EMPLOYER"))
                    .build();
            userRepo.save(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
           log.error("Error while creating user {}",userDto.getUserName(),e);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> login(UserDto userDto){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    userDto.getUserName(), userDto.getPassword()));
            String token = jwtUtils.generateToken(userDto.getUserName());
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (BadCredentialsException e){
            log.warn("Failed login attempt for user: {}", userDto.getUserName(), e);
            return new ResponseEntity<>( "Invalid Username and password", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error("Error during login", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    }

