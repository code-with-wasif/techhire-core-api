package io.github.codewithwasif.techhire.repository;

import io.github.codewithwasif.techhire.entity.JobApplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JobApplyRepo extends JpaRepository<JobApplyEntity, Long> {
}
