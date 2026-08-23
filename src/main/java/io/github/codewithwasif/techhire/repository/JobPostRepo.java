package io.github.codewithwasif.techhire.repository;

import io.github.codewithwasif.techhire.entity.JobPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface JobPostRepo extends JpaRepository<JobPostEntity, Long> {
    List<JobPostEntity> findByStatusIgnoreCase(String status);
}
