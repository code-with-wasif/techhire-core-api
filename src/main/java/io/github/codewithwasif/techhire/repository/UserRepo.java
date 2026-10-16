package io.github.codewithwasif.techhire.repository;

import io.github.codewithwasif.techhire.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUserName(String userName);
}
