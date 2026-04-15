package io.github.codewithwasif.techhire.repository;

import io.github.codewithwasif.techhire.entity.JobPostEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface JobPostRepo extends MongoRepository<JobPostEntity, String> {
    List<JobPostEntity> findByStatusIgnoreCase(String status);
}
