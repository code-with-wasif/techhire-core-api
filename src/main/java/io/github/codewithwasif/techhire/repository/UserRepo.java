package io.github.codewithwasif.techhire.repository;

import io.github.codewithwasif.techhire.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepo extends MongoRepository<UserEntity, String> {
    UserEntity findByUserName(String userName);
}
