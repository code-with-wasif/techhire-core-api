package io.github.codewithwasif.techhire.utils;

import  io.github.codewithwasif.techhire.entity.UserEntity;
import  io.github.codewithwasif.techhire.exception.ResourceNotFoundException;
import  io.github.codewithwasif.techhire.exception.UnauthorizedAccessException;
import  io.github.codewithwasif.techhire.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

    private final UserRepo userRepo;

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new UnauthorizedAccessException("Authentication token is missing or invalid");
        }

        return authentication.getName();
    }

    public UserEntity getCurrentUser() {
        String username = getCurrentUsername();
        return userRepo.findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }
}