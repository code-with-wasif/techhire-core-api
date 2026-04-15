package io.github.codewithwasif.techhire.service;

import io.github.codewithwasif.techhire.entity.UserEntity;
import io.github.codewithwasif.techhire.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsSecurity implements UserDetailsService {

    private final UserRepo userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity byUserName = userRepository.findByUserName(username);
        if(byUserName != null) {
            return org.springframework.security.core.userdetails.User.builder()
                    .username(byUserName.getUserName())
                    .password(byUserName.getPassword())
                    .roles(byUserName.getRoles().toArray(new String[0]))
                    .build();
        }
        throw new UsernameNotFoundException("User not found with username: "+username);
    }
}