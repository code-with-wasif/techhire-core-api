package io.github.codewithwasif.techhire.repository;

import io.github.codewithwasif.techhire.entity.DeveloperProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeveloperProfileRepo extends JpaRepository<DeveloperProfileEntity, Long> {

    Optional<DeveloperProfileEntity> findByDeveloperId(Long userId);

    boolean existsByDeveloperId(Long userId);
}
