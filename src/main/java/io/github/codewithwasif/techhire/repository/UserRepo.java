package io.github.codewithwasif.techhire.repository;

import io.github.codewithwasif.techhire.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepo extends JpaRepository<UserEntity, Long> {
    UserEntity findByUserName(String userName);
}
